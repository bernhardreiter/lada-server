/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest.stamm;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import de.intevation.lada.util.annotation.AuthorizationConfig;
import de.intevation.lada.util.auth.Authorization;
import de.intevation.lada.util.auth.AuthorizationType;
import de.intevation.lada.util.auth.UserInfo;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.model.master.Network;
import de.intevation.lada.rest.LadaService;

/**
 * REST service for NetzBetreiber objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("rest/netzbetreiber")
public class NetzbetreiberService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * The authorization module.
     */
    @Inject
    @AuthorizationConfig(type = AuthorizationType.HEADER)
    private Authorization authorization;

    /**
     * Get all NetzBetreiber objects.
     * <p>
     * Example: http://example.com/netzbetreiber
     *
     * @return Response object containing all NetzBetreiber objects.
     */
    @GET
    @Path("/")
    public Response get() {
        return repository.getAll(Network.class);
    }

    /**
     * Get a single NetzBetreiber object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single NetzBetreiber.
     */
    @GET
    @Path("/{id}")
    public Response getById(
        @PathParam("id") String id
    ) {
        UserInfo userInfo = authorization.getInfo();
        if (userInfo.getNetzbetreiber().contains(id)) {
            return repository.getById(Network.class, id);
        }
        return new Response(
            false, StatusCodes.CHANGED_VALUE, new ArrayList<Network>());
    }
}
