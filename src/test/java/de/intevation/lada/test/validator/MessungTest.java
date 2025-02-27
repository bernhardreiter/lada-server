/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada.test.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Assert;

import de.intevation.lada.model.lada.Measm;
import de.intevation.lada.util.data.StatusCodes;
import de.intevation.lada.validation.Validator;
import de.intevation.lada.validation.Violation;

/**
 * Test messung entities.
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@Transactional
public class MessungTest {

    //Validation keys
    private static final String MEAS_PD = "measPd";
    private static final String MEASD_ID = "measdId";
    private static final String MEASM_START_DATE = "measmStartDate";
    private static final String MIN_SAMPLE_ID = "minSampleId";

    //ID constants from test dataset
    private static final int EXISTING_SAMPLE_ID = 1000;
    private static final int EXISTING_SAMPLE_ID_SAMPLE_METH_CONT = 2000;
    private static final int EXISTING_SAMPLE_ID_REGULATION_161 = 3000;
    private static final String EXISTING_MEASD_NAME = "Mangan";
    private static final int EXISTING_MEASM_ID = 1200;
    private static final String EXISTING_MIN_SAMPLE_ID = "T100";
    private static final String EXISTING_MMT_ID = "A3";
    private static final String EXISTING_SAMPLE_START_DATE
        = "2012-05-03 13:07:00";

    //Other constants
    private static final int ID776 = 776;

    private static final SimpleDateFormat DB_UNIT_DATE_FORMAT
        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String NEW_MIN_SAMPLE_ID = "42AB";
    private static final String MIN_SAMPLE_ID_00G2 = "00G2";

    private static final String VALIDATION_KEY_SEPARATOR = "#";

    @Inject
    private Validator<Measm> validator;

    /**
     * Test nebenproben nr.
     */
    public void hasNebenprobenNr() {
        Measm messung = new Measm();
        messung.setMinSampleId("10R1");
        messung.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        if (violation.hasWarnings()) {
            Assert.assertFalse(
                violation.getWarnings().containsKey(MIN_SAMPLE_ID));
        }
    }

    /**
     * Test without nebenproben nr.
     */
    public void hasNoNebenprobenNr() {
        Measm messung = new Measm();
        messung.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getNotifications().get(MIN_SAMPLE_ID)
                .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test empty nebenproben nr.
     */
    public void hasEmptyNebenprobenNr() {
        Measm messung = new Measm();
        messung.setMinSampleId("");
        messung.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getNotifications().get(MIN_SAMPLE_ID)
                .contains(StatusCodes.VALUE_MISSING));
    }

    /**
     * Test new existing nebenproben nr.
     */
    public void existingNebenprobenNrNew() {
        Measm messung = new Measm();
        messung.setMinSampleId(EXISTING_MIN_SAMPLE_ID);
        messung.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        Assert.assertTrue(violation.hasErrors());
        Assert.assertTrue(violation.getErrors().containsKey(MIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getErrors().get(MIN_SAMPLE_ID).contains(
            StatusCodes.VALUE_AMBIGOUS));
    }

    /**
     * Test new unique nebenproben nr.
     */
    public void uniqueNebenprobenNrNew() {
        Measm messung = new Measm();
        messung.setMinSampleId(MIN_SAMPLE_ID_00G2);
        messung.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(MIN_SAMPLE_ID));
        }
    }

    /**
     * Test update unique nebenproben nr.
     */
    public void uniqueNebenprobenNrUpdate() {
        Measm messung = new Measm();
        messung.setId(EXISTING_MEASM_ID);
        messung.setSampleId(EXISTING_SAMPLE_ID);
        messung.setMinSampleId(MIN_SAMPLE_ID_00G2);
        Violation violation = validator.validate(messung);
        if (violation.hasErrors()) {
            Assert.assertFalse(
                violation.getErrors().containsKey(MIN_SAMPLE_ID));
            return;
        }
    }

    /**
     * Test update existing nebenproben nr.
     */
    public void existingNebenprobenNrUpdate() {
        Measm messung = new Measm();
        messung.setId(ID776);
        messung.setSampleId(EXISTING_SAMPLE_ID);
        messung.setMinSampleId(EXISTING_MIN_SAMPLE_ID);
        Violation violation = validator.validate(messung);
        Assert.assertTrue(violation.hasErrors());
        Assert.assertTrue(violation.getErrors().containsKey(MIN_SAMPLE_ID));
        Assert.assertTrue(
            violation.getErrors().get(MIN_SAMPLE_ID).contains(
                StatusCodes.VALUE_AMBIGOUS));
    }

    /**
     * Test measm with start date in future.
     */
    public void measmStartDateInFuture() {
        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMeasmStartDate(Date.from(tomorrow));

        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(MEASM_START_DATE));
        Assert.assertTrue(
            violation.getWarnings().get(MEASM_START_DATE).contains(
                StatusCodes.DATE_IN_FUTURE));
    }

    /**
     * Test measm with start date before sampleStartDate.
     * @throws ParseException Thrown if date parsing fails
     */
    public void measmStartDateBeforeSampleStartDate() throws ParseException {
        Instant sampleStartDate = DB_UNIT_DATE_FORMAT
            .parse(EXISTING_SAMPLE_START_DATE).toInstant();
        Instant measmStartDate = sampleStartDate.minus(1, ChronoUnit.DAYS);
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMinSampleId(NEW_MIN_SAMPLE_ID);
        measm.setMeasmStartDate(Date.from(measmStartDate));
        String warnKey = MEASM_START_DATE
            + VALIDATION_KEY_SEPARATOR + measm.getMinSampleId();
        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(warnKey));
        Assert.assertTrue(
            violation.getWarnings().get(warnKey).contains(
                StatusCodes.VALUE_NOT_MATCHING));
    }

    /**
     * Test measm with start date before sampleStartDate.
     * @throws ParseException Thrown if date parsing fails
     */
    public void measmStartDateAfterSampleStartDate() throws ParseException {
        Instant sampleStartDate = DB_UNIT_DATE_FORMAT
            .parse(EXISTING_SAMPLE_START_DATE).toInstant();
        Instant measmStartDate = sampleStartDate.plus(1, ChronoUnit.DAYS);
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMinSampleId(NEW_MIN_SAMPLE_ID);
        measm.setMeasmStartDate(Date.from(measmStartDate));
        Violation violation = validator.validate(measm);
        if (violation.hasWarnings()) {
            Assert.assertFalse(violation.getWarnings().containsKey(
                MEASM_START_DATE + VALIDATION_KEY_SEPARATOR
                + measm.getMinSampleId()));
        }
    }

    /**
     * Test measm without start date.
     */
    public void measmWithoutStartDate() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(MEASM_START_DATE));
        Assert.assertTrue(
            violation.getWarnings().get(MEASM_START_DATE).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test measm without start date connected to a sample with regulation id 1.
     */
    public void measmWithoutStartDateRegulation161Sample() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID_REGULATION_161);
        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MEASM_START_DATE));
        Assert.assertTrue(
            violation.getNotifications().get(MEASM_START_DATE).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test measm without a measPd.
     */
    public void measmWithoutMeasPD() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMinSampleId(NEW_MIN_SAMPLE_ID);

        Violation violation = validator.validate(measm);
        String warnKey = MEAS_PD + VALIDATION_KEY_SEPARATOR
            + measm.getMinSampleId();
        Assert.assertTrue(violation.hasWarnings());
        Assert.assertTrue(violation.getWarnings()
            .containsKey(warnKey));
        Assert.assertTrue(
            violation.getWarnings().get(warnKey).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test measm without a measPd connected to a sample with regulation id 1.
     */
    public void measmWithoutMeasPDRegulation161Sample() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID_REGULATION_161);
        measm.setMinSampleId(NEW_MIN_SAMPLE_ID);

        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MEAS_PD));
        Assert.assertTrue(
            violation.getNotifications().get(MEAS_PD).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test measm without a measPd connected to a continuous sample.
     */
    public void measmWithoutMeasPDRContSample() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID_SAMPLE_METH_CONT);

        Violation violation = validator.validate(measm);
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(MEAS_PD));
        Assert.assertTrue(
            violation.getNotifications().get(MEAS_PD).contains(
                StatusCodes.VALUE_MISSING));
    }

    /**
     * Test measm with measPd.
     */
    public void measmWithMeasPd() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMeasPd(1);
        Violation violation = validator.validate(measm);
        if (violation.hasWarnings()) {
            Assert.assertFalse(violation
                .getWarnings().containsKey(MEAS_PD));
        }
        if (violation.hasNotifications()) {
            Assert.assertFalse(violation.
                getNotifications().containsKey(MEAS_PD));
        }
    }

    /**
     * Test measm missing obligatory measds.
     */
    public void measmWithoutObligMeasd() {
        Measm measm = new Measm();
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMmtId(EXISTING_MMT_ID);

        Violation violation = validator.validate(measm);
        String notficationKey = MEASD_ID + VALIDATION_KEY_SEPARATOR
            + EXISTING_MEASD_NAME;
        Assert.assertTrue(violation.hasNotifications());
        Assert.assertTrue(violation.getNotifications()
            .containsKey(notficationKey));
        Assert.assertTrue(violation.getNotifications()
            .get(notficationKey).contains(StatusCodes.VAL_OBL_MEASURE));
    }

    /**
     * Test measm with all obligatory measds.
     */
    public void measmWithObligMeasd() {
        Measm measm = new Measm();
        measm.setId(EXISTING_MEASM_ID);
        measm.setSampleId(EXISTING_SAMPLE_ID);
        measm.setMmtId(EXISTING_MMT_ID);

        Violation violation = validator.validate(measm);
        if (violation.hasNotifications()) {
            String notficationKey = MEASD_ID
                + VALIDATION_KEY_SEPARATOR + EXISTING_MEASD_NAME;
            Assert.assertFalse(violation.
                getNotifications().containsKey(notficationKey));
        }
    }
}
