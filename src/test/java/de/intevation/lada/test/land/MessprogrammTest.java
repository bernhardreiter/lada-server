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
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.junit.Assert;

import de.intevation.lada.Protocol;
import de.intevation.lada.test.ServiceTest;

/**
 * Test messprogramm entities.
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
public class MessprogrammTest extends ServiceTest {
    private JsonObject expectedById;
    private JsonObject create;

    @Override
    public void init(
        URL baseUrl,
        List<Protocol> protocol
    ) {
        super.init(baseUrl, protocol);
        // Attributes with timestamps
        timestampAttributes = Arrays.asList(new String[]{
            "letzteAenderung",
            "treeModified"
        });

        // Prepare expected object
        JsonObject content =
            readJsonResource("/datasets/dbUnit_messprogramm.json");
        JsonObject messprogramm =
            content.getJsonArray("land.messprogramm").getJsonObject(0);
        JsonObjectBuilder builder = convertObject(messprogramm);
        builder.add("baId", 1);
        builder.add("intervallOffset", 0);
        builder.add("probeKommentar", JsonValue.NULL);
        builder.add("probeNehmerId", JsonValue.NULL);
        expectedById = builder.build();
        Assert.assertNotNull(expectedById);

        // Load object to test POST request
        create = readJsonResource("/datasets/messprogramm.json");
        Assert.assertNotNull(create);
    }

    /**
     * Execute the tests.
     */
    public final void execute() {
        getAll("messprogramm", "rest/messprogramm");
        getById("messprogramm", "rest/messprogramm/1000", expectedById);
        filter("messprogramm", "rest/messprogramm?qid=9");
        update(
            "messprogramm",
            "rest/messprogramm/1000",
            "mediaDesk",
            "D: 50 90 01 06 02 05 00 00 00 00 00 00",
            "D: 50 90 01 06 02 05 00 00 00 00 00 01");
        JsonObject created =
            create("messprogramm", "rest/messprogramm", create);
        delete(
            "messprogramm",
            "rest/messprogramm/" + created.getJsonObject("data").get("id"));
    }
}
