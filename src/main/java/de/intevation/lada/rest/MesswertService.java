/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import de.intevation.lada.lock.LockConfig;
import de.intevation.lada.lock.LockType;
import de.intevation.lada.lock.ObjectLocker;
import de.intevation.lada.model.land.Measm;
import de.intevation.lada.model.land.MeasVal;
import de.intevation.lada.model.land.Sample;
import de.intevation.lada.model.master.EnvMedium;
import de.intevation.lada.util.annotation.AuthorizationConfig;
import de.intevation.lada.util.auth.Authorization;
import de.intevation.lada.util.auth.AuthorizationType;
import de.intevation.lada.util.data.MesswertNormalizer;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.RequestMethod;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.validation.Validator;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationConfig;

/**
 * REST service for Messwert objects.
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
 *      "grenzwertueberschreitung": [boolean],
 *      "letzteAenderung": [timestamp],
 *      "mehId": [number],
 *      "messfehler": [number],
 *      "messgroesseId": [number],
 *      "messungsId": [number],
 *      "messwert": [number],
 *      "messwertNwg": [string],
 *      "nwgZuMesswert": [number],
 *      "owner": [boolean],
 *      "readonly":[boolean],
 *      "treeModified": [timestamp],
 *      "parentModified": [timestamp]
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
@Path("rest/messwert")
public class MesswertService extends LadaService {

    /**
     * The data repository granting read/write access.
     */
    @Inject
    private Repository repository;

    /**
     * The object lock mechanism.
     */
    @Inject
    @LockConfig(type = LockType.TIMESTAMP)
    private ObjectLocker lock;

    /**
     * The authorization module.
     */
    @Inject
    @AuthorizationConfig(type = AuthorizationType.HEADER)
    private Authorization authorization;

    @Inject
    @ValidationConfig(type = "Messwert")
    private Validator validator;

    @Inject
    private MesswertNormalizer messwertNormalizer;

    /**
     * Get Messwert objects.
     *
     * @param messungsId The requested objects have to be filtered
     * using an URL parameter named messungsId.
     * Example: http://example.com/messwert?messungsId=[ID]
     *
     * @return Response object containing filtered Messwert objects.
     * Status-Code 699 if parameter is missing or requested objects are
     * not authorized.
     */
    @GET
    @Path("/")
    public Response get(
        @QueryParam("messungsId") @NotNull Integer messungsId
    ) {
        Measm messung = repository.getByIdPlain(Measm.class, messungsId);
        if (!authorization.isAuthorized(
                messung,
                RequestMethod.GET,
                Measm.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        QueryBuilder<MeasVal> builder =
            repository.queryBuilder(MeasVal.class);
        builder.and("measmId", messungsId);
        Response r = authorization.filter(
            repository.filter(builder.getQuery()),
            MeasVal.class);
        if (r.getSuccess()) {
            @SuppressWarnings("unchecked")
            List<MeasVal> messwerts = (List<MeasVal>) r.getData();
            for (MeasVal messwert: messwerts) {
                Violation violation = validator.validate(messwert);
                if (violation.hasErrors()
                    || violation.hasWarnings()
                    || violation.hasNotifications()
                ) {
                    messwert.setErrors(violation.getErrors());
                    messwert.setWarnings(violation.getWarnings());
                    messwert.setNotifications(violation.getNotifications());
                }
            }
            return new Response(true, StatusCodes.OK, messwerts);
        } else {
            return r;
        }
    }

    /**
     * Get a Messwert object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single Messwert.
     */
    @GET
    @Path("/{id}")
    public Response getById(
        @PathParam("id") Integer id
    ) {
        Response response = repository.getById(MeasVal.class, id);
        MeasVal messwert = (MeasVal) response.getData();
        Measm messung = repository.getByIdPlain(
            Measm.class, messwert.getMeasmId());
        if (!authorization.isAuthorized(
            messung,
            RequestMethod.GET,
            Measm.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        Violation violation = validator.validate(messwert);
        if (violation.hasErrors() || violation.hasWarnings()) {
            response.setErrors(violation.getErrors());
            response.setWarnings(violation.getWarnings());
            response.setNotifications(violation.getNotifications());
        }
        return authorization.filter(
            response,
            MeasVal.class);
    }

    /**
     * Create a Messwert object.
     * <p>
     * The new object is embedded in the post data as JSON formatted string.
     * <p>
     * <pre>
     * <code>
     * {
     *  "owner": [boolean],
     *  "messungsId": [number],
     *  "messgroesseId": [number],
     *  "messwert": [number],
     *  "messwertNwg": [string],
     *  "messfehler": [number],
     *  "nwgZuMesswert": [number],
     *  "mehId": [number],
     *  "grenzwertueberschreitung": [boolean],
     *  "treeModified": null,
     *  "parentModified": null,
     *  "letzteAenderung": [date]
     * }
     * </code>
     * </pre>
     *
     * @return A response object containing the created Messwert.
     */
    @POST
    @Path("/")
    public Response create(
        MeasVal messwert
    ) {
        if (!authorization.isAuthorized(
                messwert,
                RequestMethod.POST,
                MeasVal.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        Violation violation = validator.validate(messwert);
        if (violation.hasErrors()) {
            Response response =
                new Response(false, StatusCodes.ERROR_VALIDATION, messwert);
            response.setErrors(violation.getErrors());
            response.setWarnings(violation.getWarnings());
            response.setNotifications(violation.getNotifications());
            return response;
        }

        /* Persist the new messung object*/
        Response response = repository.create(messwert);
        if (violation.hasWarnings()) {
            response.setWarnings(violation.getWarnings());
        }
        if (violation.hasNotifications()) {
           response.setNotifications(violation.getNotifications());
        }
        return authorization.filter(
            response,
            MeasVal.class);
    }

    /**
     * Update an existing Messwert object.
     * <p>
     * The object to update should come as JSON formatted string.
     * <pre>
     * <code>
     * {
     *  "id": [number],
     *  "owner": [boolean],
     *  "messungsId": [number],
     *  "messgroesseId": [number],
     *  "messwert": [number],
     *  "messwertNwg": [string],
     *  "messfehler": [number],
     *  "nwgZuMesswert": [number],
     *  "mehId": [number],
     *  "grenzwertueberschreitung": [boolean],
     *  "treeModified": [timestamp],
     *  "parentModified": [timestamp],
     *  "letzteAenderung": [date]
     * }
     * </code>
     * </pre>
     *
     * @return Response object containing the updated Messwert object.
     */
    @PUT
    @Path("/{id}")
    public Response update(
        @PathParam("id") Integer id,
        MeasVal messwert
    ) {
        if (!authorization.isAuthorized(
                messwert,
                RequestMethod.PUT,
                MeasVal.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        if (lock.isLocked(messwert)) {
            return new Response(false, StatusCodes.CHANGED_VALUE, null);
        }
        Violation violation = validator.validate(messwert);
        if (violation.hasErrors()) {
            Response response =
                new Response(false, StatusCodes.ERROR_VALIDATION, messwert);
            response.setErrors(violation.getErrors());
            response.setWarnings(violation.getWarnings());
            response.setNotifications(violation.getNotifications());
            return response;
        }

        Response response = repository.update(messwert);
        if (!response.getSuccess()) {
            return response;
        }
        if (violation.hasWarnings()) {
            response.setWarnings(violation.getWarnings());
        }
        if (violation.hasNotifications()) {
           response.setNotifications(violation.getNotifications());
        }
        return authorization.filter(
            response,
            MeasVal.class);
    }

    /**
     * Normalise all Messwert objects connected to the given Messung.
     * @param messungsId The messung id needs to be given
     * as URL parameter 'messungsId'.
     * @return Response object containing the updated Messwert objects.
     */
    @PUT
    @Path("/normalize")
    public Response normalize(
        @QueryParam("messungsId") Integer messungsId
    ) {
        if (messungsId == null) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        //Load messung, probe and umwelt to get MessEinheit to convert to
        Measm messung = repository.getByIdPlain(Measm.class, messungsId);
        if (!authorization.isAuthorized(
            messung,
            RequestMethod.PUT,
            Measm.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }

        Sample probe =
            repository.getByIdPlain(
                Sample.class, messung.getSampleId());
        if (probe.getEnvMediumId() == null
            || probe.getEnvMediumId().equals("")
        ) {
            return new Response(true, StatusCodes.OP_NOT_POSSIBLE, null);
        }
        EnvMedium umwelt =
            repository.getByIdPlain(
                EnvMedium.class, probe.getEnvMediumId());
        //Get all Messwert objects to convert
        QueryBuilder<MeasVal> messwertBuilder =
            repository.queryBuilder(MeasVal.class);
        messwertBuilder.and("measmId", messungsId);
        List<MeasVal> messwerte = messwertNormalizer.normalizeMesswerte(
            repository.filterPlain(messwertBuilder.getQuery()),
            umwelt.getId());

        for (MeasVal messwert: messwerte) {
            if (!authorization.isAuthorized(
                messwert,
                RequestMethod.PUT,
                MeasVal.class)
            ) {
                return new Response(false, StatusCodes.NOT_ALLOWED, null);
            }
            if (lock.isLocked(messwert)) {
                return new Response(false, StatusCodes.CHANGED_VALUE, null);
            }
            Violation violation = validator.validate(messwert);
            if (violation.hasErrors()) {
                Response response =
                    new Response(false, StatusCodes.ERROR_VALIDATION, messwert);
                response.setErrors(violation.getErrors());
                response.setWarnings(violation.getWarnings());
                response.setNotifications(violation.getNotifications());
                return response;
            }
            Response response = repository.update(messwert);
            if (!response.getSuccess()) {
                return response;
            }
            Response updated = repository.getById(
                MeasVal.class,
                ((MeasVal) response.getData()).getId());
            if (violation.hasWarnings()) {
                updated.setWarnings(violation.getWarnings());
            }
            if (violation.hasNotifications()) {
                updated.setNotifications(violation.getNotifications());
            }
            authorization.filter(
                    updated,
                    MeasVal.class);
        }
        return new Response(true, StatusCodes.OK, messwerte);
    }

    /**
     * Delete an existing Messwert object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(
        @PathParam("id") Integer id
    ) {
        MeasVal messwertObj = repository.getByIdPlain(MeasVal.class, id);
        if (!authorization.isAuthorized(
                messwertObj,
                RequestMethod.DELETE,
                MeasVal.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        if (lock.isLocked(messwertObj)) {
            return new Response(false, StatusCodes.NO_ACCESS, null);
        }
        /* Delete the messwert object*/
        return repository.delete(messwertObj);
    }
}
