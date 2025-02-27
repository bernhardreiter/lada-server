/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.status;

import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import de.intevation.lada.model.lada.StatusProt;
import de.intevation.lada.model.master.StatusMp;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for status.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@ValidationRule("Status")
public class StatusKombination implements Rule {

    @Inject Logger logger;

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        StatusProt status = (StatusProt) object;
        QueryBuilder<StatusMp> kombi =
            repository.queryBuilder(StatusMp.class);
        kombi.and("id", status.getStatusMpId());
        List<StatusMp> result =
            repository.filterPlain(kombi.getQuery());
        if (result.isEmpty()) {
            Violation violation = new Violation();
            violation.addError("statusMp", StatusCodes.VALUE_NOT_MATCHING);
            return violation;
        }
        return null;
    }
}
