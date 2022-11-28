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

import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.model.master.TargEnvGr;
import de.intevation.lada.rest.LadaService;

/**
 * REST service for TargEnvGr objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("rest/targenvgr")
public class SollistUmwGruppeService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * Get all TargEnvGr objects.
     *
     * @return Response object containing all TargEnvGr objects.
     */
    @GET
    @Path("/")
    public Response get() {
        return repository.getAll(TargEnvGr.class);
    }
}
