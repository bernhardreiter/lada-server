/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.messung;

import java.util.Date;

import javax.inject.Inject;

import de.intevation.lada.model.lada.Measm;
import de.intevation.lada.model.lada.Sample;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for messungen.
 * Validates if the "messzeitpunkt" is before or after the
 * "probeentnahmebeginn"
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@ValidationRule("Messung")
public class DateMesszeitpunkt implements Rule {

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Measm messung = (Measm) object;

        Sample probe = repository.getByIdPlain(
            Sample.class, messung.getSampleId());
        if (probe == null) {
            Violation violation = new Violation();
            violation.addError("sample", StatusCodes.ERROR_VALIDATION);
            return violation;
        }

        if (messung.getMeasmStartDate() == null) {
            return null;
        }

        if (messung.getMeasmStartDate().after(new Date())) {
            Violation violation = new Violation();
            violation.addWarning("measmStartDate", StatusCodes.DATE_IN_FUTURE);
            return violation;
        }

        if (probe.getSampleStartDate() == null
            && probe.getSampleEndDate() == null) {
            return null;
        }

        if ((probe.getSampleStartDate() != null
            && probe.getSampleStartDate().after(messung.getMeasmStartDate())
            || (probe.getSampleEndDate() != null
            && probe.getSampleEndDate().after(messung.getMeasmStartDate()))
            )
        ) {
            Violation violation = new Violation();
            violation.addWarning(
                "measmStartDate#" + messung.getMinSampleId(),
                StatusCodes.VALUE_NOT_MATCHING);
            return violation;
        }
        return null;
    }
}
