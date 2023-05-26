/* Copyright (C) 2020 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.exporter.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import de.intevation.lada.exporter.QueryExportJob;
import de.intevation.lada.model.lada.MeasVal;
import de.intevation.lada.model.lada.Measm;
import de.intevation.lada.exporter.ExportConfig;
import de.intevation.lada.exporter.Exporter;
import de.intevation.lada.exporter.ExportFormat;


/**
 * Job class for exporting records to a JSON file.
 *
 * @author <a href="mailto:awoestmann@intevation.de">Alexander Woestmann</a>
 */
public class JsonExportJob extends QueryExportJob {

    private static final int LENGTH = 1024;
    private String subDataJsonKey;

    /**
     * The JSON exporter.
     */
    @Inject
    @ExportConfig(format = ExportFormat.JSON)
    private Exporter exporter;

    public JsonExportJob() {
        super();
        this.format = "json";
        this.downloadFileName = "export.json";
    }

    @Override
    protected List<Map<String, Object>> mergeMessungData(
        List<Measm> messungData
    ) {
        // Create a map of id->record
        Map<Integer, Map<String, Object>> idMap = new HashMap<>();
        String sDataJsonKey = "Messungen";
        primaryData.forEach(record -> {
            idMap.put((Integer) record.get(idColumn), record);
        });

        List<Map<String, Object>> merged = primaryData;
        messungData.forEach(messung -> {
            Map<String, Object> mergedMessung = new HashMap<>();
            // Add sub data
            subDataColumns.forEach(subDataColumn -> {
                Object fieldValue = null;
                // Check if column needs seperate handling or is a valid
                // messung field
                switch (subDataColumn) {
                    case "statusKombi":
                        fieldValue = getStatusString(messung);
                        break;
                    case "messwerteCount":
                        fieldValue = getMesswertCount(messung);
                        break;
                    default:
                        fieldValue = getFieldByName(subDataColumn, messung);
                }
                mergedMessung.put(subDataColumn, fieldValue);
            });
            //Append messung to probe
            Map<String, Object> primaryRecord = idMap.get(
                messung.getSampleId());
            if (primaryRecord.get(sDataJsonKey) == null) {
                primaryRecord.put(sDataJsonKey, new ArrayList<Object>());
            }
            ArrayList<Map<String, Object>> messungenList =
                (ArrayList<Map<String, Object>>) primaryRecord.get("Messungen");
            messungenList.add(mergedMessung);
        });
        this.subDataJsonKey = sDataJsonKey;
        return merged;
    }

    @Override
    protected List<Map<String, Object>> mergeMesswertData(
        List<MeasVal> messwertData
    ) {
        // Create a map of id->record
        Map<Integer, Map<String, Object>> idMap = new HashMap<>();
        String sDataJsonKey = "messwerte";
        primaryData.forEach(record -> {
            idMap.put((Integer) record.get(idColumn), record);
        });

        List<Map<String, Object>> merged = primaryData;
        messwertData.forEach(messwert -> {
            Map<String, Object> mergedMesswert = new HashMap<>();
            // Add sub data
            subDataColumns.forEach(subDataColumn -> {
                Object fieldValue = null;
                // Check if column needs seperate handling or is a valid
                // messung field
                switch (subDataColumn) {
                    case "messungId":
                        fieldValue = getFieldByName("messungsId", messwert);
                        break;
                    default:
                        fieldValue = getFieldByName(subDataColumn, messwert);
                }
                mergedMesswert.put(subDataColumn, fieldValue);
            });
            //Append messung to probe
            Map<String, Object> primaryRecord = idMap.get(
                messwert.getMeasmId());
            if (primaryRecord.get(sDataJsonKey) == null) {
                primaryRecord.put(sDataJsonKey, new ArrayList<Object>());
            }
            ArrayList<Map<String, Object>> messwertList =
                (ArrayList<Map<String, Object>>) primaryRecord.get("messwerte");
            messwertList.add(mergedMesswert);
        });
        this.subDataJsonKey = sDataJsonKey;
        return merged;
    }


    @Override
    public void runWithTx() {
        parseExportParameters();

        // Fetch primary records
        primaryData = getQueryResult();

        List<Map<String, Object>> exportData = primaryData;
        ArrayList<String> exportColumns = new ArrayList<String>();
        exportColumns.addAll(this.columnsToExport);

        // If needed, fetch and merge sub data
        if (exportSubdata) {
            exportData = mergeSubData();
        }

        //Export data to json
        InputStream exported;
        JsonObjectBuilder optionBuilder = Json.createObjectBuilder()
            .add("subData", exportSubdata ? subDataJsonKey : "")
            .add("timezone", timezone);
        if (idColumn != null) {
            optionBuilder.add("id", idColumn);
        }
        JsonObject exportOptions = optionBuilder.build();
        exported = exporter.export(
            exportData, encoding, exportOptions, exportColumns, qId);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[LENGTH];
        int length;
        try {
            while ((length = exported.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            writeResultToFile(result.toString(encoding));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }

        logger.debug(String.format("Finished JSON export"));
    }
}
