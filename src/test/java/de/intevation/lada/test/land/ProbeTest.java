/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.test.land;

import java.net.URL;
import java.util.Arrays;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;

import de.intevation.lada.model.lada.Sample;
import de.intevation.lada.test.ServiceTest;

/**
 * Test probe entities.
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
public class ProbeTest extends ServiceTest {

    private JsonObject expectedById;
    private JsonObject create;

    @Override
    public void init(
        Client c,
        URL baseUrl
    ) {
        super.init(c, baseUrl);
        // Attributes with timestamps
        timestampAttributes = Arrays.asList(new String[]{
            "lastMod",
            "sampleStartDate",
            "schedStartDate",
            "schedEndDate",
            "treeMod"
        });

        // Prepare expected probe object
        JsonObject probe = filterJsonArrayById(
            readXmlResource("datasets/dbUnit_lada.xml", Sample.class),
            1000);
        JsonObjectBuilder builder = convertObject(probe);
        builder.addNull("midSampleDate");
        builder.addNull("sampleEndDate");
        builder.addNull("datasetCreatorId");
        builder.addNull("mpgCategId");
        builder.add("readonly", false);
        builder.add("owner", true);
        expectedById = builder.build();
        Assert.assertNotNull(expectedById);

        // Load probe object to test POST request
        create = readJsonResource("/datasets/probe.json");
        Assert.assertNotNull(create);
    }

    /**
     * Execute the tests.
     */
    public final void execute() {
        get("rest/sample", Status.METHOD_NOT_ALLOWED);
        getById("rest/sample/1000", expectedById);
        JsonObject created = create("rest/sample", create);

        final String updateFieldKey = "mainSampleId";
        final String newValue = "130510002";
        update(
            "rest/sample/1000",
            updateFieldKey,
            "120510002",
            newValue);

        // Ensure invalid envDescripDisplay is rejected
        update(
            "rest/sample/1000",
            "envDescripDisplay",
            "D: 59 04 01 00 05 05 01 02 00 00 00 00",
            "",
            Status.BAD_REQUEST);

        getAuditTrail(
            "rest/audit/probe/1000",
            updateFieldKey,
            newValue);
        delete("rest/sample/" + created.getJsonObject("data").get("id"));
    }
}
