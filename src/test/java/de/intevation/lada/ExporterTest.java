/* Copyright (C) 2021 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada;

import java.net.URL;
import java.nio.charset.CharacterCodingException;
import java.time.Duration;
import java.time.Instant;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.intevation.lada.util.data.Job;


/**
 * Test export services.
 *
 */
@RunWith(Arquillian.class)
public class ExporterTest extends BaseTest {

    private static Logger logger = Logger.getLogger(ExporterTest.class);

    private final String formatCsv = "csv";
    private final String formatJson = "json";
    private final String formatLaf = "laf";

    @PersistenceContext
    EntityManager em;

    public ExporterTest() {
        this.testDatasetName = "datasets/dbUnit_query.xml";
    }

    private JsonObjectBuilder requestJsonBuilder = Json.createObjectBuilder()
        .add("exportSubData", false)
        .add("timezone", "UTC")
        .add("columns", Json.createArrayBuilder()
            .add(Json.createObjectBuilder()
                .add("columnIndex", 0)
                .add("export", true)
                .add("filterVal", "")
                .add("isFilterActive", false)
                .add("isFilterNull", false)
                .add("isFilterNegate", false)
                .add("isFilterRegex", false)
                .add("gridColMpId", 1))
            .add(Json.createObjectBuilder()
                .add("columnIndex", 1)
                .add("export", true)
                .add("filterVal", "")
                .add("isFilterActive", false)
                .add("isFilterNull", false)
                .add("isFilterNegate", false)
                .add("isFilterRegex", false)
                .add("gridColMpId", 2))
            .add(Json.createObjectBuilder()
                .add("columnIndex", 2)
                .add("export", true)
                .add("filterVal", "")
                .add("isFilterActive", false)
                .add("isFilterNull", false)
                .add("isFilterNegate", false)
                .add("isFilterRegex", false)
                .add("gridColMpId", 4)));

    private final JsonObject measmRequestJson = Json.createObjectBuilder()
        .add("timezone", "UTC")
        .add("columns", Json.createArrayBuilder()
            .add(Json.createObjectBuilder()
                .add("columnIndex", 0)
                .add("export", true)
                .add("filterVal", "")
                .add("isFilterActive", false)
                .add("isFilterNull", false)
                .add("isFilterNegate", false)
                .add("isFilterRegex", false)
                .add("gridColMpId", 5)))
        .add("idField", "messungId")
        .add("idFilter", Json.createArrayBuilder().add("1200"))
        .add("exportSubData", true)
        .add("subDataColumns", Json.createArrayBuilder()
            .add("id")
            .add("measUnitId")
            .add("measdId"))
        .build();

    /**
     * Test asynchronous CSV export of a Sample object.
     */
    @Test
    @RunAsClient
    public final void testCsvExportProbe(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", JsonValue.NULL)
            .build();

        String result = runExportTest(baseUrl, formatCsv, requestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "hauptprobenNr,umwId,probeId\r\n"
            + "120510002,L6,1000\r\n"
            + "120510001,L6,1001\r\n",
            result);
    }

    /**
     * Test asynchronous CSV export using CSV options.
     */
    @Test
    @RunAsClient
    public final void testCsvExportOptions(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", JsonValue.NULL)
            .add("csvOptions", Json.createObjectBuilder()
                .add("fieldSeparator", "semicolon"))
            .build();

        String result = runExportTest(baseUrl, formatCsv, requestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "hauptprobenNr;umwId;probeId\r\n"
            + "120510002;L6;1000\r\n"
            + "120510001;L6;1001\r\n",
            result);
    }

    /**
     * Test asynchronous CSV export of a Sample identified by ID.
     */
    @Test
    @RunAsClient
    public final void testCsvExportProbeById(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", "hauptproben_nr")
            .add("idFilter", Json.createArrayBuilder().add("120510002"))
            .build();

        String result = runExportTest(baseUrl, formatCsv, requestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "hauptprobenNr,umwId,probeId\r\n120510002,L6,1000\r\n",
            result);
    }

    /**
     * Test asynchronous CSV export of Sample objects including measms.
     */
    @Test
    @RunAsClient
    public final void testCsvExportProbeSubData(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        JsonObject requestJson = requestJsonBuilder
            .add("idField", "probeId")
            .add("exportSubData", true)
            .add("subDataColumns", Json.createArrayBuilder()
                .add("extId")
                .add("messwerteCount"))
            .build();

        String result = runExportTest(baseUrl, formatCsv, requestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "hauptprobenNr,umwId,probeId,extId,messwerteCount\r\n"
            + "120510002,L6,1000,453,2\r\n"
            + "120510002,L6,1000,454,0\r\n"
            + "120510001,L6,1001,,\r\n",
            result);
    }

    /**
     * Test asynchronous CSV export of Measm objects including measVals.
     */
    @Test
    @RunAsClient
    public final void testCsvExportMeasmSubData(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        String result = runExportTest(
            baseUrl, formatCsv, measmRequestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "messungId,id,measUnitId,measdId\r\n"
            + "1200,1000,Sv,test\r\n"
            + "1200,1001,Sv,test\r\n",
            result);
    }

    /**
     * Test asynchronous JSON export of a Sample identified by ID.
     */
    @Test
    @RunAsClient
    public final void testJsonExportProbeById(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", "hauptproben_nr")
            .add("idFilter", Json.createArrayBuilder().add("120510002"))
            .build();

        String result = runExportTest(baseUrl, formatJson, requestJson);
        Assert.assertEquals(
            "Unexpected JSON content",
            "{\"120510002\":"
            + "{\"hauptproben_nr\":\"120510002\","
            + "\"umw_id\":\"L6\","
            + "\"probeId\":1000}}",
            result);
    }

    /**
     * Test asynchronous JSON export of a Sample object with measms.
     */
    @Test
    @RunAsClient
    public final void testJsonExportProbeSubData(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", "probeId")
            .add("idFilter", Json.createArrayBuilder().add("1000"))
            .add("exportSubData", true)
            .add("subDataColumns", Json.createArrayBuilder()
                .add("extId")
                .add("messwerteCount"))
            .build();

        String result = runExportTest(baseUrl, formatJson, requestJson);
        Assert.assertEquals(
            "Unexpected JSON content",
            "{\"1000\":"
            + "{\"hauptproben_nr\":\"120510002\","
            + "\"umw_id\":\"L6\","
            + "\"probeId\":1000,"
            + "\"Messungen\":[{\"messwerteCount\":2,\"extId\":453},"
            + "{\"messwerteCount\":0,\"extId\":454}]}}",
            result);
    }

    /**
     * Test asynchronous JSON export of a Measm object with measVals.
     */
    @Test
    @RunAsClient
    public final void testJsonExportMeasmSubData(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        String result = runExportTest(
            baseUrl, formatJson, measmRequestJson);
        Assert.assertEquals(
            "Unexpected JSON content",
            "{\"1200\":"
            + "{\"messungId\":1200,"
            + "\"messwerte\":["
            + "{\"measUnitId\":\"Sv\",\"measdId\":\"test\",\"id\":1000},"
            + "{\"measUnitId\":\"Sv\",\"measdId\":\"test\",\"id\":1001}]}}",
            result);
    }

    /**
     * Test asynchronous LAF export of a Sample identified by ID.
     */
    @Test
    @RunAsClient
    public final void testLafExportProbeById(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        final int probeId = 1000;
        JsonObject requestJson = requestJsonBuilder
            .add("proben", Json.createArrayBuilder().add(probeId))
            .build();

        String result = runExportTest(baseUrl, formatLaf, requestJson);
        Assert.assertTrue(
            "Unexpected LAF content",
            result.startsWith("%PROBE%") && result.endsWith("%ENDE%"));
    }

    /**
     * Test asynchronous export of an empty query result.
     */
    @Test
    @RunAsClient
    public final void testQueryExportEmpty(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = requestJsonBuilder
            .add("idField", "hauptproben_nr")
            .add("idFilter", Json.createArrayBuilder().add("nonexistent"))
            .build();

        String csvResult = runExportTest(
            baseUrl, formatCsv, requestJson);
        Assert.assertEquals(
            "Unexpected CSV content",
            "hauptprobenNr,umwId,probeId\r\n",
            csvResult);

        String jsonResult = runExportTest(
            baseUrl, formatJson, requestJson);
        Assert.assertEquals(
            "Unexpected JSON content",
            "{}",
            jsonResult);
    }

    /**
     * Test failing asynchronous export with invalid request payload.
     */
    @Test
    @RunAsClient
    public final void testAsyncExportFailure(
        @ArquillianResource URL baseUrl
    ) throws InterruptedException, CharacterCodingException {
        /* Request asynchronous export */
        JsonObject requestJson = Json.createObjectBuilder()
            // Add arbitrary array to avoid 404 being returned for LAF
            .add("proben", Json.createArrayBuilder().add("xxx"))
            .add("invalidField", "xxx")
            .build();

        startExport(baseUrl, formatCsv, requestJson, Job.Status.ERROR);
        startExport(baseUrl, formatJson, requestJson, Job.Status.ERROR);
        startExport(baseUrl, formatLaf, requestJson, Job.Status.ERROR);
    }

    private String startExport(
        URL baseUrl,
        String format,
        JsonObject requestJson,
        Job.Status expectedStatus
    ) throws InterruptedException {
        Response exportCreated = client.target(
            baseUrl + "data/asyncexport/" + format)
            .request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles)
            .post(Entity.entity(requestJson.toString(),
                    MediaType.APPLICATION_JSON));
        JsonObject exportCreatedObject = parseSimpleResponse(
            exportCreated);

        final String refIdKey = "refId";
        assertContains(exportCreatedObject, refIdKey);
        String refId = exportCreatedObject.getString(refIdKey);

        /* Request status of asynchronous export */
        SyncInvoker statusRequest = client.target(
            baseUrl + "data/asyncexport/status/" + refId)
            .request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles);
        JsonObject exportStatusObject = Json.createObjectBuilder().build();
        boolean done = false;
        final Instant waitUntil = Instant.now().plus(Duration.ofMinutes(1));
        final int waitASecond = 1000;
        do {
            exportStatusObject = parseSimpleResponse(statusRequest.get());

            final String doneKey = "done";
            assertContains(exportStatusObject, doneKey);
            done = exportStatusObject.getBoolean(doneKey);

            Assert.assertTrue(
                "Export not done within one minute",
                waitUntil.isAfter(Instant.now()));
            Thread.sleep(waitASecond);
        } while (!done);

        final String statusKey = "status";
        assertContains(exportStatusObject, statusKey);
        Assert.assertEquals(
            expectedStatus.name().toLowerCase(),
            exportStatusObject.getString(statusKey));

        return refId;
    }

    private String runExportTest(
        URL baseUrl, String format, JsonObject requestJson
    ) throws InterruptedException {
        String refId = startExport(
            baseUrl, format, requestJson, Job.Status.FINISHED);

        /* Request export result */
        Response download = client.target(
            baseUrl + "data/asyncexport/download/" + refId)
            .request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles)
            .get();
        Assert.assertEquals(
            "Unexpected response status code",
            Response.Status.OK.getStatusCode(),
            download.getStatus());

        return download.readEntity(String.class);
    }
}
