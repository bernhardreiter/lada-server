/* Copyright (C) 2013 by Bundesamt fuer Strahlenschutz
 * Software engineering by Intevation GmbH
 *
 * This file is Free Software under the GNU GPL (v>=3)
 * and comes with ABSOLUTELY NO WARRANTY! Check out
 * the documentation coming with IMIS-Labordaten-Application for details.
 */
package de.intevation.lada;

import java.net.URL;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.ApplyScriptBefore;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.DataSource;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.intevation.lada.model.land.Messung;
import de.intevation.lada.model.land.Probe;
import de.intevation.lada.model.stammdaten.DatensatzErzeuger;
import de.intevation.lada.model.stammdaten.Deskriptoren;
import de.intevation.lada.model.stammdaten.KoordinatenArt;
import de.intevation.lada.model.stammdaten.MessprogrammKategorie;
import de.intevation.lada.model.stammdaten.Ort;
import de.intevation.lada.model.stammdaten.Probenehmer;
import de.intevation.lada.model.stammdaten.Tag;
import de.intevation.lada.test.land.TagZuordnungTest;
import de.intevation.lada.test.stamm.DatensatzErzeugerTest;
import de.intevation.lada.test.stamm.DeskriptorenTest;
import de.intevation.lada.test.stamm.KoordinatenartTest;
import de.intevation.lada.test.stamm.MessprogrammKategorieTest;
import de.intevation.lada.test.stamm.OrtTest;
import de.intevation.lada.test.stamm.ProbenehmerTest;
import de.intevation.lada.test.stamm.Stammdaten;
import de.intevation.lada.test.stamm.TagTest;


/**
 * Class to test the Lada server stammdaten services.
 *
 * @author <a href="mailto:rrenkert@intevation.de">Raimund Renkert</a>
 */
@RunWith(Arquillian.class)
@ApplyScriptBefore("datasets/clean_and_seed.sql")
public class StammdatenTest extends BaseTest {

    private static final int T1 = 1;
    private static final int T2 = 2;
    private static final int T3 = 3;
    private static final int T4 = 4;
    private static final int T5 = 5;
    private static final int T6 = 6;
    private static final int T7 = 7;
    private static final int T8 = 8;
    private static final int T9 = 9;
    private static final int T10 = 10;
    private static final int T11 = 11;
    private static final int T12 = 12;
    private static final int T13 = 13;
    private static final int T14 = 14;
    private static final int T15 = 15;
    private static final int T16 = 16;
    private static final int T17 = 17;
    private static final int T18 = 18;
    private static final int T19 = 19;
    private static final int T20 = 20;
    private static final int T21 = 21;
    private static final int T22 = 22;
    private static final int T23 = 23;
    private static final int T24 = 24;
    private static final int T25 = 25;
    private static final int T26 = 26;
    private static final int T27 = 27;
    private static final int T28 = 28;
    private static final int T29 = 29;
    private static final int T30 = 30;
    private static final int T31 = 31;
    private static final int T32 = 32;
    private static final int T33 = 33;
    private static final int T34 = 34;
    private static final int T35 = 35;
    private static final int T36 = 36;
    private static final int T37 = 37;
    private static final int T38 = 38;
    private static final int T39 = 39;
    private static final int T40 = 40;
    private static final int T41 = 41;

    private static final int ID5 = 5;
    private static final int ID9 = 9;
    private static final int ID56 = 56;
    private static final int ID101 = 101;
    private static final int ID102 = 102;
    private static final int ID207 = 207;
    private static final int ID1000 = 1000;
    private static final int ID1801 = 1801;
    private static final int ID1901 = 1901;


    private static Logger logger = Logger.getLogger(StammdatenTest.class);

    @PersistenceContext
    EntityManager em;

    private Stammdaten stammdatenTest;
    private DatensatzErzeugerTest datensatzerzeugerTest;
    private ProbenehmerTest probenehmerTest;
    private MessprogrammKategorieTest messprogrammkategorieTest;
    private OrtTest ortTest;
    private DeskriptorenTest deskriptorenTest;
    private KoordinatenartTest kdaTest;
    private TagTest tagTest;
    private TagZuordnungTest tagZuordnungTest;

    public StammdatenTest() {
        stammdatenTest = new Stammdaten();
        datensatzerzeugerTest = new DatensatzErzeugerTest();
        probenehmerTest = new ProbenehmerTest();
        messprogrammkategorieTest = new MessprogrammKategorieTest();
        ortTest = new OrtTest();
        deskriptorenTest = new DeskriptorenTest();
        kdaTest = new KoordinatenartTest();
        tagTest = new TagTest();
        tagZuordnungTest = new TagZuordnungTest();
        verboseLogging = false;
    }

    /**
     * Output  for current test run.
     */
    @BeforeClass
    public static void beforeTests() {
        logger.info("---------- Testing Lada Stamm Services ----------");
    }

    /**
     * Insert a datensatzerzeuger object into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T1)
    @UsingDataSet("datasets/dbUnit_datensatzerzeuger.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseDatensatzerzeuger() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert datensatzerzeuger");
        protocol.addInfo("database", "Insert datensatzerzeuger into database");
        testProtocol.add(protocol);
        DatensatzErzeuger erzeuger = em.find(DatensatzErzeuger.class, ID1000);
        Assert.assertNotNull(erzeuger);
        protocol.setPassed(true);
    }

    /**
     * Tests for probe operations.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T2)
    @RunAsClient
    public final void testDatensatzerzeuger(@ArquillianResource URL baseUrl)
    throws Exception {
        datensatzerzeugerTest.init(this.client, baseUrl, testProtocol);
        datensatzerzeugerTest.execute();
    }

    /**
     * Insert a probe object into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T3)
    @UsingDataSet("datasets/dbUnit_probenehmer.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseProbenehmer() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert probenehmer");
        protocol.addInfo("database", "Insert Probenehmer into database");
        testProtocol.add(protocol);
        Probenehmer probenehmer = em.find(Probenehmer.class, ID1000);
        Assert.assertNotNull(probenehmer);
        protocol.setPassed(true);
    }

    /**
     * Tests for probe operations.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T4)
    @RunAsClient
    public final void testProbenehmer(@ArquillianResource URL baseUrl)
    throws Exception {
        probenehmerTest.init(this.client, baseUrl, testProtocol);
        probenehmerTest.execute();
    }

    /**
     * Insert a probe object into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T5)
    @UsingDataSet("datasets/dbUnit_messprogrammkategorie.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseMessprogrammKategorie() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert messprogrammkategorie");
        protocol.addInfo(
            "database",
            "Insert messprogrammkategorie into database");
        testProtocol.add(protocol);
        MessprogrammKategorie kategorie =
            em.find(MessprogrammKategorie.class, ID1000);
        Assert.assertNotNull(kategorie);
        protocol.setPassed(true);
    }

    /**
     * Tests for probe operations.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T6)
    @RunAsClient
    public final void testMessprogrammKategorie(@ArquillianResource URL baseUrl)
    throws Exception {
        messprogrammkategorieTest.init(this.client, baseUrl, testProtocol);
        messprogrammkategorieTest.execute();
    }

    /**
     * Insert a probe object into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T7)
    // Data added using clean_and_seed.sql because geometry field
    // does not work with @UsingDataSet
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseOrt() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert ort");
        protocol.addInfo("database", "Insert Ort into database");
        testProtocol.add(protocol);
        Ort ort = em.find(Ort.class, ID1000);
        Assert.assertNotNull(ort);
        protocol.setPassed(true);
    }

    /**
     * Tests for probe operations.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T8)
    @RunAsClient
    public final void testOrt(@ArquillianResource URL baseUrl)
    throws Exception {
        ortTest.init(this.client, baseUrl, testProtocol);
        ortTest.execute();
    }

    /**
     * Tests for datenbasis operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T9)
    @RunAsClient
    public final void testDatenbasisAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("datenbasis");
    }

    /**
     * Tests for datenbasis by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T10)
    @RunAsClient
    public final void testDatenbasisById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("datenbasis", ID9);
    }

    /**
     * Tests for messeinheit operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T11)
    @RunAsClient
    public final void testMesseinheitAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("messeinheit");
    }

    /**
     * Tests for messeinheit by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T12)
    @RunAsClient
    public final void testMesseinheitById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("messeinheit", ID207);
    }

    /**
     * Tests for messgroesse operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T13)
    @RunAsClient
    public final void testMessgroesseAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("messgroesse");
    }

    /**
     * Tests for messgroesse by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T14)
    @RunAsClient
    public final void testMessgroesseById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("messgroesse", ID56);
    }

    /**
     * Tests for messmethode operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T15)
    @RunAsClient
    public final void testMessmethodeAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("messmethode");
    }

    /**
     * Tests for messmethode by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T16)
    @RunAsClient
    public final void testMessmethodeById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("messmethode", "A3");
    }

    /**
     * Tests for messstelle operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T17)
    @RunAsClient
    public final void testMessstelleAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("messstelle");
    }

    /**
     * Tests for messstelle by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T18)
    @RunAsClient
    public final void testMessstelleById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("messstelle", "06010");
    }

    /**
     * Tests for netzbetreiber operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T19)
    @RunAsClient
    public final void testNetzbetreiberAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("netzbetreiber");
    }

    /**
     * Tests for netzbetreiber by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T20)
    @RunAsClient
    public final void testNetzbetreiberById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("netzbetreiber", "06");
    }

    /**
     * Tests for pflichtmessgroesse operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T21)
    @RunAsClient
    public final void testPflichtmessgroesseAll(
        @ArquillianResource URL baseUrl
    ) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("pflichtmessgroesse");
    }

    /**
     * Tests for pflichtmessgroesse by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T22)
    @RunAsClient
    public final void testPflichtmessgroesseById(
        @ArquillianResource URL baseUrl
    ) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("pflichtmessgroesse", "A3");
    }

    /**
     * Tests for probeart operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T23)
    @RunAsClient
    public final void testProbenartAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("probenart");
    }

    /**
     * Tests for probeart by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T24)
    @RunAsClient
    public final void testProbenartById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("probenart", 1);
    }

    /**
     * Tests for probenzusatz operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T25)
    @RunAsClient
    public final void testProbenzusatzAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("probenzusatz");
    }

    /**
     * Tests for probenzusatz by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T26)
    @RunAsClient
    public final void testProbenzusatzById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("probenzusatz", "A74");
    }

    /**
     * Tests for koordinatenart operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T27)
    @RunAsClient
    public final void testKoordinatenartAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("koordinatenart");
    }

    /**
     * Tests for koordinatenart by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T28)
    @RunAsClient
    public final void testKoordinatenartById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("koordinatenart", ID5);
    }

    /**
     * Tests for staat operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T29)
    @RunAsClient
    public final void testStaatAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("staat");
    }

    /**
     * Tests for staat by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T30)
    @RunAsClient
    public final void testStaatById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("staat", 0);
    }

    /**
     * Tests for umwelt  operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T31)
    @RunAsClient
    public final void testUmweltAll(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("umwelt");
    }

    /**
     * Tests for umwelt by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T32)
    @RunAsClient
    public final void testUmweltById(@ArquillianResource URL baseUrl) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("umwelt", "L6");
    }

    /**
     * Tests for verwaltungseinheit operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T33)
    @RunAsClient
    public final void testVerwaltungseinheitAll(
        @ArquillianResource URL baseUrl
    ) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getAll("verwaltungseinheit");
    }

    /**
     * Tests for verwaltungseinheit by id operations.
     * @param baseUrl The server url used for the request.
     */
    @Test
    @InSequence(T34)
    @RunAsClient
    public final void testVerwaltungseinheitById(
        @ArquillianResource URL baseUrl
    ) {
        stammdatenTest.init(this.client, baseUrl, testProtocol);
        stammdatenTest.getById("verwaltungseinheit", "11000000");
    }

    /**
     * Insert deskriptoren into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T35)
    @UsingDataSet("datasets/dbUnit_deskriptor.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseDeskriptoren() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert deskriptor");
        protocol.addInfo("database", "Insert deskriptor into database");
        testProtocol.add(protocol);
        Deskriptoren deskriptor = em.find(Deskriptoren.class, ID1000);
        Assert.assertNotNull(deskriptor);
        protocol.setPassed(true);
    }

    /**
     * Tests deskriptoren service.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T36)
    @UsingDataSet("datasets/dbUnit_pep_gen.json")
    @DataSource("java:jboss/lada-test")
    @RunAsClient
    public final void testDeskriptoren(@ArquillianResource URL baseUrl)
    throws Exception {
        deskriptorenTest.init(this.client, baseUrl, testProtocol);
        deskriptorenTest.execute();
    }

    /**
     * Insert Koordinatenart into the database.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T37)
    @UsingDataSet("datasets/dbUnit_koordinatenart.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareDatabaseKoordinatenart() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert koordinatenart");
        protocol.addInfo("database", "Insert koordinatenart into database");
        testProtocol.add(protocol);
        KoordinatenArt kda = em.find(
            KoordinatenArt.class, KoordinatenartTest.KDA_ID);
        Assert.assertNotNull(kda);
        protocol.setPassed(true);
    }

    /**
     * Tests KoordinatenartService.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T38)
    @RunAsClient
    public final void testKoordinatenart(@ArquillianResource URL baseUrl)
    throws Exception {
        kdaTest.init(this.client, baseUrl, testProtocol);
        kdaTest.execute();
    }

    /**
     * Cleanup database for TagTest.
     * @throws Exception that can occur during test
     */
    @Test
    @InSequence(T39)
    @ApplyScriptBefore("datasets/clean_and_seed.sql")
    @UsingDataSet("datasets/dbUnit_tagzuordnung.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareTag() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert for tag");
        protocol.addInfo("database",
            "Insert probe, messung and tags into database");
        testProtocol.add(protocol);
        Tag probeTag = em.find(Tag.class, ID101);
        Assert.assertNotNull(probeTag);
        Tag messungTag = em.find(Tag.class, ID102);
        Assert.assertNotNull(messungTag);
        protocol.setPassed(true);
    }

    /**
     * Test Tag service.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T40)
    @RunAsClient
    public final void testTag(@ArquillianResource URL baseUrl)
    throws Exception {
        tagTest.init(this.client, baseUrl, testProtocol);
        tagTest.execute();
    }

    /**
     * Insert objects needed for the tagzuordnung test into the db.
     * @throws Exception that can occur during test
     */
    @Test
    @InSequence(T40)
    @ApplyScriptBefore("datasets/clean_and_seed.sql")
    @UsingDataSet("datasets/dbUnit_tagzuordnung.json")
    @DataSource("java:jboss/lada-test")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public final void prepareTagZuordnung() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("database");
        protocol.setType("insert for tagzuordnung");
        protocol.addInfo("database",
            "Insert probe, messung and tags into database");
        testProtocol.add(protocol);
        Probe probe = em.find(Probe.class, ID1901);
        Assert.assertNotNull(probe);
        Messung messung = em.find(Messung.class, ID1801);
        Assert.assertNotNull(messung);
        Tag probeTag = em.find(Tag.class, ID101);
        Assert.assertNotNull(probeTag);
        Tag messungTag = em.find(Tag.class, ID102);
        Assert.assertNotNull(messungTag);
        protocol.setPassed(true);
    }

    /**
     * Test TagZuordnung service.
     * @param baseUrl The server url used for the request.
     * @throws Exception that can occur during the test.
     */
    @Test
    @InSequence(T41)
    @RunAsClient
    public final void testTagZuordnung(@ArquillianResource URL baseUrl)
    throws Exception {
        tagZuordnungTest.init(this.client, baseUrl, testProtocol);
        tagZuordnungTest.execute();
    }
}
