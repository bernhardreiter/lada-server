/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest.stamm;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import de.intevation.lada.model.stammdaten.Nuts;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.rest.Response;


/**
 * REST service for UmweltZustz objects.
 * <p>
 * The services produce data in the application/json media type.
 * A typical response holds information about the action performed and the data.
 * <pre>
 * <code>
 * {
 *  "success": [boolean];
 *  "message": [string],
 *  "data":[{
 *      "id": [string],
 *      "staatId": [number],
 *      "bezeichnug": [number],
 *      "kdaId": [number],
 *      "koordXExtern": [number],
 *      "koordYExtern": [number],
 *  }],
 *  "errors": [object],
 *  "warnings": [object],
 *  "readonly": [boolean],
 *  "totalCount": [number]
 * }
 * </code>
 * </pre>
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("rest/nuts")
@RequestScoped
public class NutsService {
    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * Get all UmweltZusatz objects.
     * <p>
     * Example: http://example.com/umweltzustz
     *
     * @return Response object containing all StatusWert objects.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
        @Context HttpHeaders headers,
        @Context UriInfo info
    ) {
        return repository.getAll(Nuts.class);
    }

    /**
     * Get a single Staat object by id.
     * <p>
     * The id is appended to the URL as a path parameter.
     * <p>
     * Example: http://example.com/staat/{id}
     *
     * @return Response object containing a single Staat.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(
        @Context HttpHeaders headers,
        @PathParam("id") String id
    ) {
        return repository.getById(Nuts.class, id);
    }
}
