/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.messung;

import javax.inject.Inject;

import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;
import de.intevation.lada.model.lada.Measm;
import de.intevation.lada.model.lada.Sample;
import de.intevation.lada.util.data.Repository;
/**
 * Validation rule for messung.
 * Validates if the messung has a "messbeginn".
 *
 *
 */
@ValidationRule("Messung")
public class HasMessbeginn implements Rule {

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Measm messung = (Measm) object;
        Sample probe =
            repository.getByIdPlain(Sample.class, messung.getSampleId());

        if (messung.getMeasmStartDate() == null
            && probe != null
            && probe.getRegulationId() != null
            && probe.getRegulationId() != 1
        ) {
            Violation violation = new Violation();
            violation.addWarning("measmStartDate", StatusCodes.VALUE_MISSING);
            return violation;
        } else if (messung.getMeasmStartDate() == null) {
            Violation violation = new Violation();
            violation.addNotification(
                "measmStartDate", StatusCodes.VALUE_MISSING);
            return violation;
        } else {
            return null;
        }
    }

}
