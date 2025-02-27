/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.model.master.AdminUnit;

/**
 * REST service for AdminUnit objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("adminunit")
public class AdminUnitService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * Get AdminUnit objects.
     *
     * @param name The result list can be filtered using the URL parameter
     * 'query'. A filter is defined as the first letters of the 'name'.
     * Might be null (i.e. not given at all) but not an empty string.
     *
     * @return Response containing requested objects.
     */
    @GET
    public Response get(
        @QueryParam("name") @Pattern(regexp = ".+") String name
    ) {
        if (name == null) {
            return repository.getAll(AdminUnit.class);
        }
        QueryBuilder<AdminUnit> builder =
            repository.queryBuilder(AdminUnit.class);
        builder.andLike("name", name + "%");
        return repository.filter(builder.getQuery());
    }

    /**
     * Get a single AdminUnit object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single AdminUnit.
     */
    @GET
    @Path("{id}")
    public Response getById(
        @PathParam("id") String id
    ) {
        return repository.getById(AdminUnit.class, id);
    }
}
