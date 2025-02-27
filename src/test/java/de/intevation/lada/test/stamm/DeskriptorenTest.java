/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.test.stamm;

import java.net.URL;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;

import org.junit.Assert;

import de.intevation.lada.model.master.EnvDescrip;
import de.intevation.lada.test.ServiceTest;

/**
 * Test Deskriptor entities.
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
public class DeskriptorenTest extends ServiceTest {

    private JsonObject expectedById;

    @Override
    public void init(
        Client c,
        URL baseUrl
    ) {
        super.init(c, baseUrl);

        // Prepare expected object
        JsonObject content =
            readXmlResource("datasets/dbUnit_master.xml", EnvDescrip.class)
            .getJsonObject(0);
        JsonObjectBuilder builder = convertObject(content);
        expectedById = builder.build();
        Assert.assertNotNull(expectedById);
    }

    /**
     * Execute the tests.
     */
    public final void execute() {
        get("rest/envdescrip?lev=1");
        get("rest/envdescrip?lev=1&predId=1&predId=2");
        getById("rest/envdescrip/1000", expectedById);
    }
}
