/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.messung;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.intevation.lada.model.land.Messung;
import de.intevation.lada.model.land.Messwert;
import de.intevation.lada.model.land.Probe;
import de.intevation.lada.model.stammdaten.Messgroesse;
import de.intevation.lada.model.stammdaten.PflichtMessgroesse;
import de.intevation.lada.util.annotation.RepositoryConfig;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.RepositoryType;
import de.intevation.lada.util.data.Strings;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for messungen.
 * Validates if the messung has all "pflichtmessgroessen".
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@ValidationRule("Messung")
public class HasPflichtmessgroessen implements Rule {

    @Inject
    @RepositoryConfig(type=RepositoryType.RO)
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Messung messung = (Messung)object;
        Probe probe = repository.getByIdPlain(Probe.class, messung.getProbeId(), Strings.LAND);

        QueryBuilder<PflichtMessgroesse> builder =
            new QueryBuilder<PflichtMessgroesse>(
                repository.entityManager(Strings.STAMM),
                PflichtMessgroesse.class);
        builder.and("messMethodeId", messung.getMmtId());
        builder.and("umwId", probe.getUmwId());
        Response response = repository.filter(builder.getQuery(), Strings.STAMM);
        @SuppressWarnings("unchecked")
        List<PflichtMessgroesse> pflicht =
            (List<PflichtMessgroesse>)response.getData();

        QueryBuilder<Messwert> wertBuilder =
            new QueryBuilder<Messwert>(
                repository.entityManager(Strings.LAND), Messwert.class);
        wertBuilder.and("messungsId", messung.getId());
        Response wertResponse =
            repository.filter(wertBuilder.getQuery(), Strings.LAND);
        @SuppressWarnings("unchecked")
        List<Messwert> messwerte = (List<Messwert>)wertResponse.getData();
        Violation violation = new Violation();
        List<PflichtMessgroesse> tmp = new ArrayList<PflichtMessgroesse>();
        for (Messwert wert : messwerte) {
            for (PflichtMessgroesse p : pflicht) {
                if (p.getMessgroesseId().equals(wert.getMessgroesseId())) {
                    tmp.add(p);
                }
            }
        }
        pflicht.removeAll(tmp);
        if (!pflicht.isEmpty()) {
            for (PflichtMessgroesse p : pflicht) {
                Messgroesse mg = repository.getByIdPlain(Messgroesse.class, p.getMessgroesseId(), Strings.STAMM);
                violation.addWarning("pflichtmessgroesse#" + mg.getMessgroesse(), 631);
            }
        }
        return violation.hasWarnings() ? violation : null;
    }
}
