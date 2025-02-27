/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import de.intevation.lada.model.lada.Mpg;
import de.intevation.lada.model.lada.MpgMmtMp;
import de.intevation.lada.model.master.Measd;
import de.intevation.lada.util.annotation.AuthorizationConfig;
import de.intevation.lada.util.auth.Authorization;
import de.intevation.lada.util.auth.AuthorizationType;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.RequestMethod;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.validation.Validator;
import de.intevation.lada.validation.Violation;

/**
 * REST service for MpgMmtMp objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("mpgmmtmp")
public class MpgMmtMpService extends LadaService {

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

    @Inject
    private Validator<MpgMmtMp> validator;

    /**
     * Get MpgMmtMp objects.
     *
     * @param mpgId The requested objects will be filtered
     * using a URL parameter named mpgId.
     *
     * @return Response containing requested objects.
     */
    @GET
    public Response get(
        @QueryParam("mpgId") @NotNull Integer mpgId
    ) {
        QueryBuilder<MpgMmtMp> builder =
            repository.queryBuilder(MpgMmtMp.class);
        builder.and("mpgId", mpgId);
        return authorization.filter(
            repository.filter(builder.getQuery()),
            MpgMmtMp.class);
    }

    /**
     * Get a MpgMmtMp object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single MpgMmtMp.
     */
    @GET
    @Path("{id}")
    public Response getById(
        @PathParam("id") Integer id
    ) {
        return authorization.filter(
            repository.getById(MpgMmtMp.class, id),
            MpgMmtMp.class);
    }

    /**
     * Create a MpgMmtMp object.
     * @return A response object containing the created MpgMmtMp.
     */
    @POST
    public Response create(
        MpgMmtMp messprogrammmmt
    ) {
        if (!authorization.isAuthorized(
                messprogrammmmt,
                RequestMethod.POST,
                MpgMmtMp.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        Violation violation = validator.validate(messprogrammmmt);
        if (violation.hasErrors()) {
            Response response = new Response(
                false, StatusCodes.ERROR_VALIDATION, messprogrammmmt);
            response.setErrors(violation.getErrors());
            response.setWarnings(violation.getWarnings());
            return response;
        }

        setMessgroesseObjects(messprogrammmmt);

        /* Persist the new messprogrammmmt object*/
        return authorization.filter(
            repository.create(messprogrammmmt),
            MpgMmtMp.class);
    }

    /**
     * Update an existing MpgMmtMp object.
     *
     * @return Response object containing the updated MpgMmtMp object.
     */
    @PUT
    @Path("{id}")
    public Response update(
        @PathParam("id") Integer id,
        MpgMmtMp messprogrammmmt
    ) {
        if (!authorization.isAuthorized(
                messprogrammmmt,
                RequestMethod.PUT,
                MpgMmtMp.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        Violation violation = validator.validate(messprogrammmmt);
        if (violation.hasErrors()) {
            Response response = new Response(
                false, StatusCodes.ERROR_VALIDATION, messprogrammmmt);
            response.setErrors(violation.getErrors());
            response.setWarnings(violation.getWarnings());
            return response;
        }

        setMessgroesseObjects(messprogrammmmt);

        return authorization.filter(
            repository.update(messprogrammmmt),
            MpgMmtMp.class);
    }

    /**
     * Delete an existing MessprogrammMmt object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object.
     */
    @DELETE
    @Path("{id}")
    public Response delete(
        @PathParam("id") Integer id
    ) {
        MpgMmtMp messprogrammmmtObj = repository.getByIdPlain(
            MpgMmtMp.class, id);
        if (!authorization.isAuthorized(
                messprogrammmmtObj,
                RequestMethod.DELETE,
                Mpg.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        /* Delete the messprogrammmmt object*/
        return repository.delete(messprogrammmmtObj);
    }

    /**
     * Initialize referenced objects from given IDs.
     */
    private void setMessgroesseObjects(MpgMmtMp mm) {
        Set<Measd> mos = new HashSet<>();
        for (Integer mId: mm.getMeasds()) {
            Measd m = repository.getByIdPlain(Measd.class, mId);
            if (m != null) {
                mos.add(m);
            }
        }
        mm.setMeasdObjects(mos);
    }
}
