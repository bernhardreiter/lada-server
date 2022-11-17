/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest.stamm;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.intevation.lada.model.stammdaten.TargActMmtGr;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.rest.LadaService;

/**
 * REST service for SollistMmtGrp objects.
 * <p>
 * The services produce data in the application/json media type.
 * A typical response holds information about the action performed and the data.
 * <pre>
 * <code>
 * {
 *  "success": [boolean];
 *  "message": [string],
 *  "data":[{
 *      "id": [number],
 *      "beschreibung": [string],
 *      "bezeichnung": [string]
 *  }],
 *  "errors": [object],
 *  "warnings": [object],
 *  "notifications": [object],
 *  "readonly": [boolean],
 *  "totalCount": [number]
 * }
 * </code>
 * </pre>
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("rest/sollistmmtgruppe")
public class SollistMmtGruppeService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * Get all SollistMmtGrp objects.
     * <p>
     * Example: http://example.com/sollistmmtgruppe
     *
     * @return Response object containing all SollistMmtGrp objects.
     */
    @GET
    @Path("/")
    public Response get() {
        return repository.getAll(TargActMmtGr.class);
    }
}
