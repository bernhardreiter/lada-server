/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.ort;

import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.locationtech.jts.geom.Point;

import de.intevation.lada.model.master.AdminBorderView;
import de.intevation.lada.model.master.Site;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for ort.
 * Validates if the coordinates are in the specified "Verwaltungseinheit".
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@ValidationRule("Ort")
public class CoordinatesInVE implements Rule {

    @Inject
    private Logger logger;

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Site ort = (Site) object;
        String gemId = "".equals(ort.getAdminUnitId())
            ? null
            : ort.getAdminUnitId();

        if (gemId != null && ort.getGeom() != null) {
            QueryBuilder<AdminBorderView> vg = repository
                .queryBuilder(AdminBorderView.class)
                .and("municId", gemId);
            List<AdminBorderView> vgs = repository.filterPlain(vg.getQuery());
            if (vgs == null || vgs.isEmpty()) {
                Violation violation = new Violation();
                violation.addWarning(
                    "municId", StatusCodes.GEO_COORD_UNCHECKED);
                return violation;
            }

            Point p = ort.getGeom();
            if (p == null) {
                logger.error("geom is null. "
                    + "Probably OrtFactory.transformCoordinates() has not "
                    + "been called on this ort.");
            }
            Boolean unscharf = ort.getIsFuzzy();
            Violation violation = new Violation();
            for (AdminBorderView singlevg : vgs) {
                if (singlevg.getShape().contains(p)) {
                    if (unscharf != null && !unscharf) {
                        return null;
                    } else {
                        ort.setIsFuzzy(false);
                        return null;
                    }
                } else {
                    double dist = singlevg.getShape().distance(p);
                    dist = dist * (3.1415926 / 180) * 6378137;
                    if (dist < 1000) {
                        ort.setIsFuzzy(true);
                        return null;
                    } else {
                        ort.setIsFuzzy(false);
                        violation.addWarning(
                            "coordXExt", StatusCodes.GEO_POINT_OUTSIDE);
                        violation.addWarning(
                            "coordYExt", StatusCodes.GEO_POINT_OUTSIDE);
                        return violation;
                    }
                }
            }

            violation.addWarning("coordXExt", StatusCodes.GEO_NOT_MATCHING);
            violation.addWarning("coordYExt", StatusCodes.GEO_NOT_MATCHING);
            return violation;
        }
        return null;
    }
}
