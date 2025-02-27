/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */

package de.intevation.lada.importer;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.intevation.lada.importer.laf.LafImportJob;
import de.intevation.lada.model.master.MeasFacil;
import de.intevation.lada.util.auth.UserInfo;
import de.intevation.lada.util.data.JobManager;


/**
 * Class managing import jobs.
 */
public class ImportJobManager extends JobManager {

    @Inject
    private Provider<LafImportJob> lafImportJobProvider;

    public ImportJobManager() {
        logger.debug("Creating ImportJobManager");
    };

    /**
     * Create a new import job.
     * @param userInfo User info
     * @param params Parameters
     * @param mst MessStelle
     * @return New job refId
     */
    public String createImportJob(
        UserInfo userInfo, JsonObject params, MeasFacil mst
    ) {
        LafImportJob newJob = lafImportJobProvider.get();
        newJob.setJsonInput(params);
        newJob.setUserInfo(userInfo);
        newJob.setMst(mst);

        newJob.setFuture(executor.submit(newJob));
        return addJob(newJob);
    }

    /**
     * Get the import result for the job with given refId.
     * @param id Refid
     * @return Result as json string
     * @throws JobNotFoundException Thrown if job with given id was not found
     */
    public String getImportResult(String id) throws JobNotFoundException {
        LafImportJob job = (LafImportJob) getJobById(id);
        String jsonString = "";
        logger.debug(String.format("Returning result for job %s", id));
        try {
            Map<String, Map<String, Object>> importData = job.getImportData();
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(importData);
        } catch (JsonProcessingException jpe) {
            logger.error(
                String.format("Error returning result for job %s:", id));
            jpe.printStackTrace();
        } finally {
            removeJob(id);
        }
        return jsonString;
    }
}
