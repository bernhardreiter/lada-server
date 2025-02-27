/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.model.master.NuclFacilGr;
import de.intevation.lada.model.master.NuclFacilGrMp;

/**
 * REST service for NuclFacilGr objects.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Path("nuclfacilgr")
public class NuclFacilGrService extends LadaService {

    /**
     * The data repository granting read access.
     */
    @Inject
    private Repository repository;

    /**
     * Get NuclFacilGr objects.
     *
     * @param nuclFacilId URL parameter to filter by nuclFacilId
     * @return Response containing the requested objects.
     */
    @GET
    public Response get(
        @QueryParam("nuclFacilId") Integer nuclFacilId
    ) {
        if (nuclFacilId == null) {
            return repository.getAll(NuclFacilGr.class);
        }
        QueryBuilder<NuclFacilGrMp> builder =
            repository.queryBuilder(NuclFacilGrMp.class);
        builder.and("nuclFacilId", nuclFacilId);
        List<NuclFacilGrMp> zuord =
            repository.filterPlain(builder.getQuery());
        if (zuord.isEmpty()) {
            return new Response(true, StatusCodes.OK, null);
        }
        QueryBuilder<NuclFacilGr> builder1 =
            repository.queryBuilder(NuclFacilGr.class);
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < zuord.size(); i++) {
            ids.add(zuord.get(i).getNuclFacilGrId());
        }
        builder1.orIn("id", ids);
        return repository.filter(builder1.getQuery());
    }

    /**
     * Get a single NuclFacilGr object by id.
     *
     * @param id The id is appended to the URL as a path parameter.
     * @return Response object containing a single NuclFacilGr.
     */
    @GET
    @Path("{id}")
    public Response getById(
        @PathParam("id") Integer id
    ) {
        return repository.getById(NuclFacilGr.class, id);
    }
}
