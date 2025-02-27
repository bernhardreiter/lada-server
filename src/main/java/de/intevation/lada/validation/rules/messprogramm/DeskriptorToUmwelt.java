/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.validation.rules.messprogramm;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.intevation.lada.model.lada.Mpg;
import de.intevation.lada.model.master.EnvDescrip;
import de.intevation.lada.model.master.EnvDescripEnvMediumMp;
import de.intevation.lada.util.data.QueryBuilder;
import de.intevation.lada.util.data.Repository;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.util.rest.Response;
import de.intevation.lada.validation.Violation;
import de.intevation.lada.validation.annotation.ValidationRule;
import de.intevation.lada.validation.rules.Rule;

/**
 * Validation rule for probe.
 * Validates if the umwelt id fits the deskriptor string.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@ValidationRule("Messprogramm")
public class DeskriptorToUmwelt implements Rule {

    @Inject
    private Repository repository;

    @Override
    public Violation execute(Object object) {
        Mpg messprogramm = (Mpg) object;
        if (messprogramm.getEnvDescripDisplay() == null
            || messprogramm.getEnvDescripDisplay().equals("")
        ) {
            return null;
        }
        if (messprogramm.getEnvMediumId() == null) {
            return null;
        }
        String[] mediaDesk = messprogramm.getEnvDescripDisplay().split(" ");
        if (mediaDesk.length <= 1) {
            return null;
        }
        List<Integer> mediaIds = new ArrayList<Integer>();
        boolean zebs = false;
        Integer parent = null;
        Integer hdParent = null;
        Integer ndParent = null;
        if ("01".equals(mediaDesk[1])) {
            zebs = true;
        }
        for (int i = 1; i < mediaDesk.length; i++) {
            if ("00".equals(mediaDesk[i])) {
                mediaIds.add(-1);
                continue;
            }
            if (zebs && i < 5) {
                parent = hdParent;
            } else if (!zebs && i < 3) {
                parent = hdParent;
            } else {
                parent = ndParent;
            }
            QueryBuilder<EnvDescrip> builder = repository.queryBuilder(
                 EnvDescrip.class);
            if (parent != null) {
                builder.and("predId", parent);
            }
            builder.and("levVal", mediaDesk[i]);
            builder.and("lev", i - 1);
            Response response = repository.filter(
                builder.getQuery());
            @SuppressWarnings("unchecked")
            List<EnvDescrip> data = (List<EnvDescrip>) response.getData();
            if (data.isEmpty()) {
                Violation violation = new Violation();
                violation.addWarning("envDescripDisplay", StatusCodes.VAL_DESK);
                return violation;
            }
            hdParent = data.get(0).getId();
            mediaIds.add(data.get(0).getId());
            if (i == 2) {
                ndParent = data.get(0).getId();
            }
        }
        Violation violation =
            validateUmwelt(mediaIds, messprogramm.getEnvMediumId(), zebs, 0);
        return violation;
    }

    private Violation validateUmwelt(
        List<Integer> media,
        String umwId,
        boolean isZebs,
        int ndx
    ) {
        QueryBuilder<EnvDescripEnvMediumMp> builder =
            repository.queryBuilder(EnvDescripEnvMediumMp.class);

        if (media.size() == 0) {
            Violation violation = new Violation();
            violation.addWarning(
                "envMediumId#" + umwId, StatusCodes.VALUE_NOT_MATCHING);
            return violation;
        }

        int size = 1;
        for (int i = size; i >= 0; i--) {
            String field = "s" + (i > 9 ? i : "0" + i);
            QueryBuilder<EnvDescripEnvMediumMp> tmp = builder.getEmptyBuilder();
            if (media.get(i) != -1) {
                tmp.and(field, media.get(i));
                tmp.or(field, null);
                builder.and(tmp);
            } else {
                builder.and(field, null);
            }
        }
        Response response =
            repository.filter(builder.getQuery());
        @SuppressWarnings("unchecked")
        List<EnvDescripEnvMediumMp> data =
            (List<EnvDescripEnvMediumMp>) response.getData();
        if (data.isEmpty()) {
            Violation violation = new Violation();
            violation.addWarning(
                "envMediumId#" + umwId, StatusCodes.VALUE_NOT_MATCHING);
            return violation;
        }

        boolean unique = isUnique(data);
        if (unique && umwId.equals(data.get(0).getEnvMediumId())) {
            return null;
        } else if (unique && !umwId.equals(data.get(0).getEnvMediumId())) {
            Violation violation = new Violation();
            violation.addWarning(
                "envMediumId#" + umwId, StatusCodes.VALUE_NOT_MATCHING);
            return violation;
        } else {
            Violation violation = new Violation();
            violation.addWarning(
                "envMediumId#" + umwId, StatusCodes.VALUE_NOT_MATCHING);

            int found = -1;
            int lastMatch = -12;
            for (int i = 0; i < data.size(); i++) {
                int matches = -12;
                for (int j = size; j < 12; j++) {
                    switch (j) {
                        case 1: if (media.get(1).equals(data.get(i).getS01())
                                    || media.get(1).equals(-1)
                                    && data.get(i).getS01() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 2: if (media.get(2).equals(data.get(i).getS02())
                                    || media.get(2).equals(-1)
                                    && data.get(i).getS02() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 3: if (media.get(3).equals(data.get(i).getS03())
                                    || media.get(3).equals(-1)
                                    && data.get(i).getS03() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 4: if (media.get(4).equals(data.get(i).getS04())
                                    || media.get(4).equals(-1)
                                    && data.get(i).getS04() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 5: if (media.get(5).equals(data.get(i).getS05())
                                    || media.get(5).equals(-1)
                                    && data.get(i).getS05() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 6: if (media.get(6).equals(data.get(i).getS06())
                                    || media.get(6).equals(-1)
                                    && data.get(i).getS06() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 7: if (media.get(7).equals(data.get(i).getS07())
                                    || media.get(7).equals(-1)
                                    && data.get(i).getS07() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 8: if (media.get(8).equals(data.get(i).getS08())
                                    || media.get(8).equals(-1)
                                    && data.get(i).getS08() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 9: if (media.get(9).equals(data.get(i).getS09())
                                    || media.get(9).equals(-1)
                                    && data.get(i).getS09() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 10: if (media.get(10).equals(data.get(i).getS10())
                                    || media.get(10).equals(-1)
                                    && data.get(i).getS10() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        case 11: if (media.get(11).equals(data.get(i).getS11())
                                    || media.get(11).equals(-1)
                                    && data.get(i).getS11() == null
                                ) {
                                    matches += 1;
                                }
                                break;
                        default: break;
                    }
                    if (matches > lastMatch) {
                        lastMatch = matches;
                        found = i;
                    }
                }
            }
            if (found >= 0 && data.get(found).getEnvMediumId().equals(umwId)) {
                return null;
            }
            return violation;
        }
    }

    private boolean isUnique(List<EnvDescripEnvMediumMp> list) {
        if (list.isEmpty()) {
            return false;
        }
        String element = list.get(0).getEnvMediumId();
        for (int i = 1; i < list.size(); i++) {
            if (!element.equals(list.get(i).getEnvMediumId())) {
                return false;
            }
        }
        return true;
    }
}
