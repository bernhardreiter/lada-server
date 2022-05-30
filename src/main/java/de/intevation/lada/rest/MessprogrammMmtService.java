/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import de.intevation.lada.model.land.Messprogramm;
import de.intevation.lada.model.land.MessprogrammMmt;
import de.intevation.lada.util.annotation.AuthorizationConfig;
import de.intevation.lada.util.auth.Authorization;
import de.intevation.lada.util.auth.AuthorizationType;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.RequestMethod;
import de.intevation.lada.util.rest.Response;

/**
 * REST service for MessprogrammMmt objects.
 * <p>
 * The services produce data in the application/json media type.
 * All HTTP methods use the authorization module to determine if the user is
 * allowed to perform the requested action.
 * A typical response holds information about the action performed and the data.
 * <pre>
 * <code>
 * {
 *  "success": [boolean];
 *  "message": [string],
 *  "data":[{
 *      "id": [number],
 *      "letzteAenderung": [timestamp],
 *      "messgroessen": [array],
 *      "mmtId": [string],
 *      "messprogrammId": [number]
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
@Path("rest/messprogrammmmt")
public class MessprogrammMmtService extends LadaService {

    /**
     * The data repository granting read/write access.
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
     * Get MessprogrammMmt objects.
     *
     * @param messprogrammId The requested objects can be filtered
     * using a URL parameter named messprogrammId.
     * Example: http://example.com/messprogrammmmt?messprogrammId=[ID]
     *
     * @return Response containing requested objects.
     */
    @GET
    @Path("/")
    public Response get(
        @QueryParam("messprogrammId") Integer messprogrammId
    ) {
        if (messprogrammId == null) {
            return repository.getAll(MessprogrammMmt.class);
        }
        QueryBuilder<MessprogrammMmt> builder =
            repository.queryBuilder(MessprogrammMmt.class);
        builder.and("messprogrammId", messprogrammId);
        return authorization.filter(
            repository.filter(builder.getQuery()),
            MessprogrammMmt.class);
    }

    /**
     * Get a MessprogrammMmt object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single MessprogrammMmt.
     */
    @GET
    @Path("/{id}")
    public Response getById(
        @PathParam("id") Integer id
    ) {
        return authorization.filter(
            repository.getById(MessprogrammMmt.class, id),
            MessprogrammMmt.class);
    }

    /**
     * Create a MessprogrammMmt object.
     * <p>
     * The new object is embedded in the post data as JSON formatted string.
     * <p>
     * <pre>
     * <code>
     * {
     *  "messprogrammId": [number],
     *  "mmtId": [string],
     *  "messgroessen": [array],
     *  "letzteAenderung": [date]
     * }
     * </code>
     * </pre>
     *
     * @return A response object containing the created MessprogrammMmt.
     */
    @POST
    @Path("/")
    public Response create(
        MessprogrammMmt messprogrammmmt
    ) {
        if (!authorization.isAuthorized(
                messprogrammmmt,
                RequestMethod.POST,
                MessprogrammMmt.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        /* Persist the new messprogrammmmt object*/
        return authorization.filter(
            repository.create(messprogrammmmt),
            MessprogrammMmt.class);
    }

    /**
     * Update an existing MessprogrammMmt object.
     * <p>
     * The object to update should come as JSON formatted string.
     * <pre>
     * <code>
     * {
     *  "id": [number],
     *  "messprogrammId": [number],
     *  "mmtId": [string],
     *  "messgroessen": [array],
     *  "letzteAenderung": [date]
     * }
     * </code>
     * </pre>
     *
     * @return Response object containing the updated MessprogrammMmt object.
     */
    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") Integer id,
        MessprogrammMmt messprogrammmmt
    ) {
        if (!authorization.isAuthorized(
                messprogrammmmt,
                RequestMethod.PUT,
                MessprogrammMmt.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        Response response = repository.update(messprogrammmmt);
        if (!response.getSuccess()) {
            return response;
        }
        return authorization.filter(
            response,
            MessprogrammMmt.class);
    }

    /**
     * Delete an existing MessprogrammMmt object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(
        @PathParam("id") Integer id
    ) {
        MessprogrammMmt messprogrammmmtObj = repository.getByIdPlain(
            MessprogrammMmt.class, id);
        if (!authorization.isAuthorized(
                messprogrammmmtObj,
                RequestMethod.DELETE,
                Messprogramm.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        /* Delete the messprogrammmmt object*/
        return repository.delete(messprogrammmmtObj);
    }
}
