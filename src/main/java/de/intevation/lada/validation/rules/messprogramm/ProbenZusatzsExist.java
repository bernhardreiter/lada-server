/* Copyright (C) 2022 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.messprogramm;

import javax.inject.Inject;

import de.intevation.lada.model.lada.Mpg;
import de.intevation.lada.model.master.SampleSpecif;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for Messprogramm.
 * Validates if ProbenZusatz objects exist.
 */
@ValidationRule("Messprogramm")
public class ProbenZusatzsExist implements Rule {

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Mpg messprogramm = (Mpg) object;
        Violation violation = new Violation();

        if (messprogramm.getSampleSpecifs() != null) {
            for (SampleSpecif pz: messprogramm.getSampleSpecifs()) {
                if (repository.getByIdPlain(
                        SampleSpecif.class, pz.getId()) == null
                ) {
                    violation.addError(
                        "sampleSpecifs", StatusCodes.NOT_EXISTING);
                }
            }
        }

        return violation.hasErrors()
            ? violation
            : null;
    }
}
