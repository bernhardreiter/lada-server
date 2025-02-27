/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.ort;

import javax.inject.Inject;

import java.util.List;

import de.intevation.lada.model.master.NuclFacil;
import de.intevation.lada.model.master.NuclFacilGrMp;
import de.intevation.lada.model.master.Site;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;



@ValidationRule("Ort")
public class ValidREIMesspunkt implements Rule {

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Site ort = (Site) object;

        Violation violation = new Violation();
        if ( ort == null || ort.getSiteClassId()==null || ort.getSiteClassId() != 3)  {
            return null;
        }

        if (ort.getNuclFacilGrId()!=null) {
            QueryBuilder<NuclFacilGrMp> builder =
                repository.queryBuilder(NuclFacilGrMp.class);
            builder.and("nuclFacilGrId", ort.getNuclFacilGrId());
            List<NuclFacilGrMp> ktas = (List<NuclFacilGrMp>) repository.filterPlain(builder.getQuery());

            //Compare first 4 characters of Ort ID to stored KTAs
            if ((ort.getExtId() == null || ort.getExtId().length() < 4 ) || ktas.size() < 1) {
                violation.addWarning("extId", StatusCodes.VALUE_OUTSIDE_RANGE);
            } else {
                String KTAOrtId = ort.getExtId().substring(0,4);
                QueryBuilder<NuclFacil> builderKtaList =
                    repository.queryBuilder(NuclFacil.class);
                    builderKtaList.and("extId", KTAOrtId);
                List<NuclFacil> KtaList = repository.filterPlain(builderKtaList.getQuery());

                if (KtaList.size() < 1 || KtaList == null) {
                    violation.addWarning("extId", StatusCodes.ORT_ANLAGE_MISSING);
                    return violation;
                }

                for (NuclFacilGrMp kta : ktas){
                    if ( kta.getNuclFacilId() != KtaList.get(0).getId() ) {
                        violation.addWarning("reiNuclFacilGrId", StatusCodes.VALUE_NOT_MATCHING);
                    } else if ( ort.getExtId().length() < 5
                        && kta.getNuclFacilId() == KtaList.get(0).getId() ){
                        violation.addWarning("extId", StatusCodes.ORT_REIMP_MISSING);
                    } else if (  ort.getExtId().length() > 12  && kta.getNuclFacilId() == KtaList.get(0).getId() ){
                        violation.addWarning("extId", StatusCodes.ORT_REIMP_TOO_LONG);
                    } else {
                        break;
                    }
                }

            }

        } else {
                violation.addWarning("nuclFacilGrId", StatusCodes.VALUE_MISSING);
        }
        if (violation.hasErrors() || violation.hasWarnings()) {
            return violation;
        } else {
            return null;
        }
    }
}
