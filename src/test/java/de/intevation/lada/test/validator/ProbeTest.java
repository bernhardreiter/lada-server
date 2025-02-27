/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.test.validator;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Assert;

import de.intevation.lada.model.lada.Sample;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Validator;
import de.intevation.lada.validation.Violation;

/**
 * Test probe validations.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Transactional
public class ProbeTest {


    //Validation keys
    private static final String ENTNAHME_ORT = "entnahmeOrt";
    private static final String ENV_DESCRIP_DISPLAY = "envDescripDisplay";
    private static final String ENV_MEDIUM_ID = "envMediumId";
    private static final String EXT_ID = "extId";
    private static final String GEOLOCAT_R = "REIMesspunkt";
    private static final String GEOLOCAT_U = "ursprungsOrt";
    private static final String MAIN_SAMPLE_ID = "mainSampleId";
    private static final String NUCL_FACIL_GR_ID = "nuclFacilGrId";
    private static final String ORIG_DATE = "origDate";
    private static final String REI_AG_GR_ID = "reiAgGrId";
    private static final String SAMPLE_START_DATE = "sampleStartDate";
    private static final String SAMPLE_END_DATE = "sampleEndDate";
    private static final String SAMPLE_SPECIF_MEAS_VAL
        = "sample_specif_meas_val";
    private static final String SAMPLE_METH_ID = "sampleMethId";

    //Test data ids
    private static final String ENV_MEDIUM_L6 = "L6";
    private static final String ENV_MEDIUM_L42 = "L42";
    private static final Integer EXAMPLE_REI_AG_GR_ID = 1;
    private static final Integer EXAMPLE_NUCL_FACIL_ID = 1;
    private static final String MST_06010 = "06010";
    private static final Integer REGULATION_ID_161 = 1;
    private static final Integer REGULATION_ID_REI = 4;
    private static final Integer SAMPLE_METH_ID_INDIVIDUAL = 1;
    private static final Integer SAMPLE_METH_ID_S = 3;
    private static final Integer SAMPLE_METH_ID_CONT = 9;

    //Other constants
    private static final long TS1 = 1376287046510L;
    private static final long TS2 = 1376287046511L;
    private static final long TS3 = 2376287046511L;
    private static final int ID710 = 710;
    private static final int ID1000 = 1000;
    private static final int ID2000 = 2000;
    private static final int ID3000 = 3000;

    private static final String EXISTING_MAIN_SAMPLE_ID = "120510002";
    private static final String NEW_MAIN_SAMPLE_ID = "4564567890";
    private static final String EXAMPLE_ENV_DESCRIP_FROM_SAMPLE_DATA
        = "D: 10 11 12";
    private static final String EXAMPLE_ENV_DESCRIP_DISPLAY
        = "D: 01 59 03 01 01 02 05 01 02 00 00 00";


    @Inject
    private Validator<Sample> validator;

    /**
     * Test hauptprobennr.
     */
    public void hasHauptprobenNr() {
        Sample probe = new Sample();
        probe.setMainSampleId("4554567890");
        Violation violation = validator.validate(probe);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(MAIN_SAMPLE_ID));
        }
    }

    /**
     * Test no hauptprobennr.
     */
    public void hasNoHauptprobenNr() {
        Sample probe = new Sample();
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MAIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getNotifications().get(MAIN_SAMPLE_ID).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test new existing hpnr.
     */
    public void existingHauptprobenNrNew() {
        Sample probe = new Sample();
        probe.setMainSampleId(EXISTING_MAIN_SAMPLE_ID);
        probe.setMeasFacilId(MST_06010);
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasErrors());
        Assert.assertTrue(violation.getErrors().containsKey(MAIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getErrors().get(MAIN_SAMPLE_ID).contains(
                StatusCodes.VALUE_AMBIGOUS));
    }

    /**
     * Test new unique hpnr.
     */
    public void uniqueHauptprobenNrNew() {
        Sample probe = new Sample();
        probe.setMainSampleId(NEW_MAIN_SAMPLE_ID);
        Violation violation = validator.validate(probe);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(MAIN_SAMPLE_ID));
        }
    }

    /**
     * Test update unique hpnr.
     */
    public void uniqueHauptprobenNrUpdate() {
        Sample probe = new Sample();
        probe.setId(1);
        probe.setMainSampleId(NEW_MAIN_SAMPLE_ID);
        Violation violation = validator.validate(probe);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(MAIN_SAMPLE_ID));
        }
    }

    /**
     * Test update of existing hpnr..
     */
    public void existingHauptprobenNrUpdate() {
        Sample probe = new Sample();
        probe.setId(1);
        probe.setMainSampleId(EXISTING_MAIN_SAMPLE_ID);
        probe.setMeasFacilId(MST_06010);
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasErrors());
        Assert.assertTrue(violation.getErrors().containsKey(MAIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getErrors().get(MAIN_SAMPLE_ID).contains(
                StatusCodes.VALUE_AMBIGOUS));
    }

    /**
     * Test entnahmeort.
     */
    public void hasEntnahmeOrt() {
        Sample probe = new Sample();
        probe.setId(ID1000);
        Violation violation = validator.validate(probe);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(ENTNAHME_ORT));
        }
    }

    /**
     * Test no entnahmeort.
     */
    public void hasNoEntnahmeOrt() {
        Sample probe = new Sample();
        probe.setId(ID710);
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings().containsKey(ENTNAHME_ORT));
        Assert.assertTrue(
            violation.getWarnings().get(ENTNAHME_ORT).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test probenahmebegin.
     */
    public void hasProbeentnahmeBegin() {
        Sample probe = new Sample();
        probe.setSampleStartDate(new Timestamp(TS1));
        probe.setSampleEndDate(new Timestamp(TS2));
        Violation violation = validator.validate(probe);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(SAMPLE_START_DATE));
        }
    }

    /**
     * Test no probenahme begin.
     */
    public void hasNoProbeentnahmeBegin() {
        Sample probe = new Sample();
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(
            violation.getWarnings().containsKey(SAMPLE_START_DATE));
        Assert.assertTrue(
            violation.getWarnings().get(SAMPLE_START_DATE).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test probenahme begin without end.
     */
    public void timeNoEndProbeentnahmeBegin() {
        Sample probe = new Sample();
        probe.setSampleStartDate(new Timestamp(TS1));
        Violation violation = validator.validate(probe);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(SAMPLE_START_DATE));
        }
    }

    /**
     * Test probenahme begin without begin.
     */
    public void timeNoBeginProbeentnahmeBegin() {
        Sample probe = new Sample();
        probe.setSampleEndDate(new Timestamp(TS1));
        Violation violation = validator.validate(probe);
        Assert.assertTrue(
            violation.getWarnings().get(SAMPLE_START_DATE).contains(
                StatusCodes.VALUE_MISSING));
        Assert.assertTrue(
            violation.getWarnings().get(SAMPLE_START_DATE).contains(
                StatusCodes.DATE_BEGIN_AFTER_END));
    }

    /**
     * Test probenahme begin after end.
     */
    public void timeBeginAfterEndProbeentnahmeBegin(
    ) {
        Sample probe = new Sample();
        probe.setSampleStartDate(new Timestamp(TS2));
        probe.setSampleEndDate(new Timestamp(TS1));
        Violation violation = validator.validate(probe);
        Assert.assertTrue(
            violation.getWarnings().get(SAMPLE_START_DATE).contains(
                StatusCodes.DATE_BEGIN_AFTER_END));
    }

    /**
     * Test probenahmebegin in future.
     */
    public void timeBeginFutureProbeentnahmeBegin() {
        Sample probe = new Sample();
        probe.setSampleStartDate(new Timestamp(TS3));
        Violation violation = validator.validate(probe);
        Assert.assertTrue(
            violation.getWarnings().get(SAMPLE_START_DATE).contains(
            StatusCodes.DATE_IN_FUTURE));
    }

    /**
     * Test umwelt.
     */
    public void hasUmwelt() {
        Sample probe = new Sample();
        probe.setEnvMediumId("A4");
        Violation violation = validator.validate(probe);
        if (violation.hasWarnings()) {
            Assert.assertFalse(violation.getWarnings()
                .containsKey(ENV_MEDIUM_ID));
        }
    }

    /**
     * Test no umwelt.
     */
    public void hasNoUmwelt() {
        Sample probe = new Sample();
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings().containsKey(ENV_MEDIUM_ID));
        Assert.assertTrue(violation.getWarnings().get(ENV_MEDIUM_ID).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test empty umwelt.
     */
    public void hasEmptyUmwelt() {
        Sample probe = new Sample();
        probe.setEnvMediumId("");
        Violation violation = validator.validate(probe);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings().containsKey(ENV_MEDIUM_ID));
        Assert.assertTrue(violation.getWarnings().get(ENV_MEDIUM_ID).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test sampleMeth validation.
     */
    public void peBeginEqualsPeEnd() {
        Instant now = Instant.now();
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setSampleMethId(SAMPLE_METH_ID_INDIVIDUAL);
        sample.setSampleStartDate(Date.from(now));
        sample.setSampleEndDate(Date.from(now));

        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(SAMPLE_METH_ID));
        }
    }

    /**
     * Test sampleMeth validation.
     */
    public void peBeginDoesNotEqualPeEnd() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);

        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setSampleMethId(SAMPLE_METH_ID_INDIVIDUAL);
        sample.setSampleStartDate(Date.from(yesterday));
        sample.setSampleEndDate(Date.from(now));

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings().containsKey(SAMPLE_METH_ID));
        Assert.assertTrue(violation.getWarnings()
            .get(SAMPLE_METH_ID).contains(StatusCodes.VAL_SINGLE_DATE));
    }

    /**
     * Test orig date is after sample start date.
     */
    public void origDateAfterSampleStartDate() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Sample sample = new Sample();
        sample.setSampleStartDate(Date.from(yesterday));
        sample.setOrigDate(Date.from(now));

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings().containsKey(ORIG_DATE));
        Assert.assertTrue(violation.getWarnings()
            .get(ORIG_DATE).contains(StatusCodes.URSPR_DATE_BEFORE_BEGIN));
    }

    /**
     * Test orig date is before sample start date.
     */
    public void origDateBeforeSampleStartDate() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Sample sample = new Sample();
        sample.setSampleStartDate(Date.from(now));
        sample.setOrigDate(Date.from(yesterday));

        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(ORIG_DATE));
        }
    }

    /**
     * Test sample with valid envDescripDisplay.
     */
    public void envDescripDisplay() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay(EXAMPLE_ENV_DESCRIP_FROM_SAMPLE_DATA);
        sample.setRegulationId(1);

        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            boolean valDeskWarn =
                violation.getWarnings().containsKey(ENV_DESCRIP_DISPLAY)
                && violation.getWarnings()
                    .get(ENV_DESCRIP_DISPLAY).contains(StatusCodes.VAL_DESK);
            Assert.assertFalse(valDeskWarn);
        }
    }

    /**
     * Test sample without envDescripDisplay.
     */
    public void noEnvDescripDisplay() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay(null);

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(ENV_DESCRIP_DISPLAY));
        Assert.assertTrue(violation.getWarnings().get(ENV_DESCRIP_DISPLAY)
            .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test sample with empty envDescripDisplay.
     */
    public void emptyEnvDescripDisplay() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay("");

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(ENV_DESCRIP_DISPLAY));
        Assert.assertTrue(violation.getWarnings().get(ENV_DESCRIP_DISPLAY)
            .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test sample with envDescripDisplay S1 not set.
     */
    public void envDescripDisplayS1NotSet() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay("00 00 00 00");
        sample.setRegulationId(REGULATION_ID_REI);

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(ENV_DESCRIP_DISPLAY));
        Assert.assertTrue(violation.getWarnings().get(ENV_DESCRIP_DISPLAY)
            .contains(StatusCodes.VAL_S1_NOTSET));
    }

    /**
     * Test sample with inval envDescripDisplay.
     */
    public void envDescripDisplayInvalidDisplayString() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay("77 88 99 00");
        sample.setRegulationId(REGULATION_ID_REI);

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(ENV_DESCRIP_DISPLAY));
        Assert.assertTrue(violation.getWarnings().get(ENV_DESCRIP_DISPLAY)
            .contains(StatusCodes.VAL_DESK));
    }

    /**
     * Test sample with matching envMediumId.
     */
    public void envDescripWithMatchingEnvMediumId() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay(EXAMPLE_ENV_DESCRIP_DISPLAY);
        sample.setEnvMediumId("N71");
        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey("envMediumId#N71"));
        }
    }

    /**
     * Test sample without matching envMediumId.
     */
    public void envDescripWithoutMatchingEnvMediumId() {
        Sample sample = new Sample();
        sample.setEnvDescripDisplay(EXAMPLE_ENV_DESCRIP_FROM_SAMPLE_DATA);
        sample.setEnvMediumId("L54");
        String warningKey = "envMediumId#L54";
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(warningKey));
        Assert.assertTrue(violation.getWarnings().get(warningKey)
            .contains(StatusCodes.VALUE_NOT_MATCHING));
    }

    /**
     * Test sample with single U Type geolocat.
     */
    public void hasSingleUTypeGeolocat() {
        Sample sample = new Sample();
        sample.setId(ID1000);
        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(GEOLOCAT_U));
        }
    }

    /**
     * Test sample with sampleEndDate.
     */
    public void hasSampleEndDate() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Sample sample = new Sample();
        sample.setSampleStartDate(Date.from(yesterday));
        sample.setSampleEndDate(Date.from(now));
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setSampleMethId(SAMPLE_METH_ID_CONT);

        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(SAMPLE_END_DATE));
        }
    }

    /**
     * Test sample without sample end date.
     */
    public void hasNoSampleEndDate() {
        Instant now = Instant.now();
        Sample sample = new Sample();
        sample.setSampleStartDate(Date.from(now));
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setSampleMethId(SAMPLE_METH_ID_CONT);

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(SAMPLE_END_DATE));
        Assert.assertTrue(violation.getWarnings().get(SAMPLE_END_DATE)
            .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test sample without sample end date.
     */
    public void hasSampleEndDateBeforeBegin() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Sample sample = new Sample();
        sample.setSampleStartDate(Date.from(now));
        sample.setSampleEndDate(Date.from(yesterday));
        sample.setRegulationId(REGULATION_ID_161);
        sample.setSampleMethId(SAMPLE_METH_ID_S);

        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(SAMPLE_END_DATE));
        Assert.assertTrue(violation.getWarnings().get(SAMPLE_END_DATE)
            .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test REI sample with R type geolocat.
     */
    public void hasRTypeGeolocat() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setId(ID3000);
        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(GEOLOCAT_R));
        }
    }

    /**
     * Test REI sample with no R type geolocat.
     */
    public void hasNoRTypeGeolocat() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setId(ID2000);
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(GEOLOCAT_R));
        Assert.assertTrue(violation.getWarnings().get(GEOLOCAT_R)
            .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test sample without REI regulation but REI fields.
     */
    public void sampleShouldNotHaveREIData() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_161);
        sample.setReiAgGrId(EXAMPLE_REI_AG_GR_ID);
        sample.setNuclFacilGrId(EXAMPLE_NUCL_FACIL_ID);
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasErrors());
        boolean hasBothErrorKeys =
            violation.getErrors().containsKey(REI_AG_GR_ID)
            && violation.getErrors().containsKey(NUCL_FACIL_GR_ID);
        Assert.assertTrue(hasBothErrorKeys);
        boolean hasCorrectErrorValues =
            violation.getErrors().get(REI_AG_GR_ID)
                .contains(StatusCodes.VALUE_NOT_MATCHING)
            && violation.getErrors().get(NUCL_FACIL_GR_ID)
                .contains(StatusCodes.VALUE_NOT_MATCHING);
        Assert.assertTrue(hasCorrectErrorValues);
    }

    /**
     * Test sample with REI regulation but without REI fields.
     */
    public void sampleShouldHaveREIData() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasErrors());
        boolean hasBothWarningKeys =
            violation.getWarnings().containsKey(REI_AG_GR_ID)
            && violation.getWarnings().containsKey(NUCL_FACIL_GR_ID);
        Assert.assertTrue(hasBothWarningKeys);
        boolean hasCorrectWarningValues =
            violation.getWarnings().get(REI_AG_GR_ID)
                .contains(StatusCodes.VALUE_MISSING)
            && violation.getWarnings().get(NUCL_FACIL_GR_ID)
                .contains(StatusCodes.VALUE_MISSING);
        Assert.assertTrue(hasCorrectWarningValues);
    }

    /**
     * Test REI sample with REI data.
     */
    public void sampleWithREIData() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setReiAgGrId(EXAMPLE_REI_AG_GR_ID);
        sample.setNuclFacilGrId(EXAMPLE_NUCL_FACIL_ID);
        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(REI_AG_GR_ID));
            Assert.assertFalse(
                violation.getWarnings().containsKey(NUCL_FACIL_GR_ID));
        }
    }

    /**
     * Test REI sample without matching envMedium.
     */
    public void reiSampleWithoutMatchingEnvMedium() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setReiAgGrId(EXAMPLE_REI_AG_GR_ID);
        sample.setEnvMediumId(ENV_MEDIUM_L42);
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(ENV_MEDIUM_ID));
        Assert.assertTrue(violation.getWarnings().get(ENV_MEDIUM_ID)
            .contains(StatusCodes.VAL_UWB_NOT_MATCHING_REI));
    }

    /**
     * Test rei sample with matching envMediumId.
     */
    public void reiSampleWithMatchingEnvMedium() {
        Sample sample = new Sample();
        sample.setRegulationId(REGULATION_ID_REI);
        sample.setReiAgGrId(EXAMPLE_REI_AG_GR_ID);
        sample.setEnvMediumId(ENV_MEDIUM_L6);

        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(ENV_MEDIUM_ID)
                && violation.getWarnings()
                    .get(ENV_MEDIUM_ID)
                    .contains(StatusCodes.VAL_UWB_NOT_MATCHING_REI));
        }
    }

    /**
     * Test sample with sample specif but without matching env medium.
     */
    public void sampleSpecifMesValWithoutMatchingEnvMedium() {
        Sample sample = new Sample();
        sample.setId(ID1000);
        sample.setEnvMediumId(ENV_MEDIUM_L42);
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(SAMPLE_SPECIF_MEAS_VAL));
        Assert.assertTrue(violation.getWarnings().get(SAMPLE_SPECIF_MEAS_VAL)
            .contains(StatusCodes.VAL_PZW));

    }

    /**
     * Test sample with samplespecif with matching env medium.
     */
    public void sampleSpecifMesValWithMatchingEnvMedium() {
        Sample sample = new Sample();
        sample.setId(ID1000);
        sample.setEnvMediumId(ENV_MEDIUM_L6);
        Violation violation = validator.validate(sample);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(SAMPLE_SPECIF_MEAS_VAL)
                && violation.getWarnings()
                    .get(SAMPLE_SPECIF_MEAS_VAL)
                    .contains(StatusCodes.VAL_PZW));
        }
    }

    /**
     * Test sample without a unique extId.
     */
    public void noUniqueExtId() {
        Sample sample = new Sample();
        sample.setExtId("sample_ext_id");
        Violation violation = validator.validate(sample);
        Assert.assertTrue(violation.hasErrors());
        Assert.assertTrue(violation.getErrors()
            .containsKey(EXT_ID));
        Assert.assertTrue(violation.getErrors().get(EXT_ID)
            .contains(StatusCodes.VALUE_AMBIGOUS));
    }

    /**
     * Test sample with unique ext id.
     */
    public void uniqueExtId() {
        Sample sample = new Sample();
        sample.setExtId("SomethingUnique");
                Violation violation = validator.validate(sample);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(EXT_ID));
        }
    }
}
