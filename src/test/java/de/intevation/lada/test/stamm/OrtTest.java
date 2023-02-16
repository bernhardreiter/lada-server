/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.test.stamm;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import de.intevation.lada.BaseTest;
import de.intevation.lada.Protocol;
import de.intevation.lada.test.ServiceTest;

/**
 * Test ort entities.
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
public class OrtTest extends ServiceTest {

    private JsonObject expectedById;
    private JsonObject create;
    private byte[] imgBytes;
    private byte[] mapBytes;

    @Override
    public void init(
        Client c,
        URL baseUrl,
        List<Protocol> protocol
    ) {
        super.init(c, baseUrl, protocol);
        // Attributes with timestamps
        timestampAttributes = Arrays.asList(new String[]{
            "letzteAenderung"
        });
        // Attributes with point geometries
        geomPointAttributes = Arrays.asList(new String[]{
                "geom"
        });

        // Prepare expected object
        JsonObject content = readJsonResource("/datasets/dbUnit_ort.json");
        JsonObject erzeuger =
            content.getJsonArray("master.site").getJsonObject(0);
        JsonObjectBuilder builder = convertObject(erzeuger);
        expectedById = builder.build();
        Assert.assertNotNull(expectedById);

        // Load object to test POST request
        create = readJsonResource("/datasets/ort.json");
        Assert.assertNotNull(create);

        //Create dummy image bytes
        imgBytes = "siteImage".getBytes();
        mapBytes = "siteMap".getBytes();
    }

    /**
     * Test the site image upload function.
     *
     * Passes if:
     *   - An image can be uploaded using bytes
     *   - The bytes received via the get interface equal the uploaded bytes
     * @param bytes Bytes to use for tests
     * @param parameter Url parameter
     */
    private void testUploadImage(byte[] bytes, String parameter) {
        //Upload image
        Protocol prot = new Protocol();
        prot.setName("site image service");
        prot.setType("create");
        prot.setPassed(false);
        protocol.add(prot);

        WebTarget postTarget = client.target(baseUrl + parameter);
        Response postResponse = postTarget.request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles)
            .post(Entity.entity(bytes, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        Assert.assertEquals(200, postResponse.getStatus());

        //Get image
        WebTarget target = client.target(baseUrl + parameter);
        Response response = target.request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles)
            .get();
        byte[] responseBytes = response.readEntity(byte[].class);
        Assert.assertArrayEquals(responseBytes, bytes);

        WebTarget deleteTarget = client.target(baseUrl + parameter);
        Response deleteResponse = deleteTarget.request()
            .header("X-SHIB-user", BaseTest.testUser)
            .header("X-SHIB-roles", BaseTest.testRoles)
            .delete();
        prot.setPassed(deleteResponse.getStatus() == 200);
    }

    /**
     * Execute the tests.
     */
    public final void execute() {
        get("ort", "rest/site");
        getById("ort", "rest/site/1000", expectedById);
        int createdId = create("site", "rest/site", create)
            .getJsonObject("data").getInt("id");
        update("site", "rest/site/" + createdId,
            "longText", "Langer Text", "Längerer Text");
        //Test site images
        testUploadImage(imgBytes, "rest/site/" + createdId + "/img");
        testUploadImage(mapBytes, "rest/site/" + createdId + "/map");
        delete("site", "rest/site/" + createdId);
    }
}
