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
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import de.intevation.lada.model.lada.Sample;
import de.intevation.lada.model.master.Sampler;
import de.intevation.lada.util.annotation.AuthorizationConfig;
import de.intevation.lada.util.auth.Authorization;
import de.intevation.lada.util.auth.AuthorizationType;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.RequestMethod;
import de.intevation.lada.util.rest.Response;

/**
 * REST service for Sampler objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("sampler")
public class SamplerService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    @Inject
    @AuthorizationConfig(type = AuthorizationType.HEADER)
    private Authorization authorization;

    /**
     * Get all Sampler objects.
     *
     * @return Response object containing all objects.
     */
    @GET
    public Response get() {
        List<Sampler> nehmer =
            repository.getAllPlain(Sampler.class);
        for (Sampler p : nehmer) {
            // TODO Do not iterate all the objects if its not necessary
            p.setReadonly(true);
                // !authorization.isAuthorized(
                //     p,
                //     RequestMethod.POST,
                //     Probenehmer.class));
        }
        return new Response(true, StatusCodes.OK, nehmer, nehmer.size());
    }

    /**
     * Get a single Sampler object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single object.
     */
    @GET
    @Path("{id}")
    public Response getById(
        @PathParam("id") Integer id
    ) {
        Sampler p = repository.getByIdPlain(Sampler.class, id);
        p.setReadonly(
            !authorization.isAuthorized(
                p,
                RequestMethod.POST,
                Sampler.class
            )
        );
        List<Sample> referencedProbes = getPRNZuordnungs(p);
        p.setReferenceCount(referencedProbes.size());
        return new Response(true, StatusCodes.OK, p);
    }

    @POST
    public Response create(
        @Valid Sampler probenehmer
    ) {
        if (!authorization.isAuthorized(
            probenehmer,
            RequestMethod.POST,
            Sampler.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, probenehmer);
        }
        QueryBuilder<Sampler> builder =
            repository.queryBuilder(Sampler.class);
        builder.and("extId", probenehmer.getExtId());
        builder.and("networkId", probenehmer.getNetworkId());
        List<Sampler> nehmer =
            repository.filterPlain(builder.getQuery());
        if (nehmer.isEmpty()) {
            return repository.create(probenehmer);
        }
        return new Response(false, StatusCodes.IMP_DUPLICATE, null);
    }

    @PUT
    @Path("{id}")
    public Response update(
        @PathParam("id") Integer id,
        @Valid Sampler probenehmer
    ) {
        if (!authorization.isAuthorized(
            probenehmer,
            RequestMethod.PUT,
            Sampler.class)
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, probenehmer);
        }
        QueryBuilder<Sampler> builder =
            repository.queryBuilder(Sampler.class);
        builder.and("extId", probenehmer.getExtId());
        builder.and("networkId", probenehmer.getNetworkId());
        List<Sampler> nehmer =
            repository.filterPlain(builder.getQuery());
        if (!nehmer.isEmpty()
            && !nehmer.get(0).getId().equals(probenehmer.getId())
        ) {
            return new Response(false, StatusCodes.IMP_DUPLICATE, null);
        }
        return repository.update(probenehmer);
    }

    @DELETE
    @Path("{id}")
    public Response delete(
        @PathParam("id") Integer id
    ) {
        Sampler probenehmer = repository.getByIdPlain(
            Sampler.class, id);
        if (probenehmer == null
            || !authorization.isAuthorized(
                probenehmer,
                RequestMethod.DELETE,
                Sampler.class
            )
        ) {
            return new Response(false, StatusCodes.NOT_ALLOWED, null);
        }
        if (getPRNZuordnungs(probenehmer).size() > 0) {
            return new Response(false, StatusCodes.ERROR_DELETE, probenehmer);
        }
        return repository.delete(probenehmer);
    }

    private List<Sample> getPRNZuordnungs(Sampler probenehmer) {
            //check for references
            QueryBuilder<Sample> refBuilder =
            repository.queryBuilder(Sample.class);
            refBuilder.and("samplerId", probenehmer.getId());
            return repository.filterPlain(refBuilder.getQuery());
    }
}
