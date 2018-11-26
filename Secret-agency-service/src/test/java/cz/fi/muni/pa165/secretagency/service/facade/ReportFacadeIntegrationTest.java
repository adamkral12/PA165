package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportUpdateTextDTO;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.*;
import cz.fi.muni.pa165.secretagency.facade.ReportFacade;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


/**
 * Integration tests for report facade. These tests are validating that layers are communicating correctly.
 *
 * @author Jan Pavlu
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReportFacadeIntegrationTest extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private ReportFacade reportFacade;

    @Autowired
    private BeanMappingService beanMappingService;

    private Report transferBabisJrToKrymReport;
    private Agent babis;
    private Mission transferBabisJrToKrym;
    private Department praha;

    @BeforeClass
    public void setUp() {
        EntityManager em1 = emf.createEntityManager();
        em1.getTransaction().begin();

        setUpReport();
        setUpAgent();
        setUpMission();
        setUpDepartment();

        em1.persist(praha);
        em1.persist(babis);
        em1.persist(transferBabisJrToKrym);
        em1.persist(transferBabisJrToKrymReport);
        em1.getTransaction().commit();
        em1.close();
    }

    private void setUpReport() {
        transferBabisJrToKrymReport = new Report();
        transferBabisJrToKrymReport.setText("Get rid of my son");
        transferBabisJrToKrymReport.setReportStatus(ReportStatus.NEW);
        transferBabisJrToKrymReport.setDate(LocalDate.of(2018, 11, 1));
        transferBabisJrToKrymReport.setMissionResult(MissionResultReportEnum.COMPLETED);
    }

    private void setUpAgent() {
        babis = new Agent();
        babis.setLanguages(Collections.singleton(LanguageEnum.SK));
        babis.setRank(AgentRankEnum.AGENT_IN_CHARGE);
        babis.setName("Andrej Babis");
        babis.setCodeName("Bures 2");
        babis.setBirthDate(LocalDate.of(1980, 8, 8));
    }

    private void setUpMission() {
        transferBabisJrToKrym = new Mission();
        transferBabisJrToKrym.setMissionType(MissionTypeEnum.SABOTAGE);
        transferBabisJrToKrym.setStarted(LocalDate.of(2017, 5, 1));
        transferBabisJrToKrym.setEnded(LocalDate.of(2018, 11, 1));
        transferBabisJrToKrym.setLatitude(47.304324);
        transferBabisJrToKrym.setLongitude(39.521161);
        transferBabisJrToKrym.addAgent(babis);
        transferBabisJrToKrym.addReport(transferBabisJrToKrymReport, babis);
    }

    private void setUpDepartment() {
        praha = new Department();
        praha.setCountry("Czech Republic");
        praha.setCity("Praha");
        praha.setSpecialization(DepartmentSpecialization.INTELLIGENCE);
        praha.setLatitude(50.08804);
        praha.setLongitude(14.42076);
        praha.addAgent(babis);
    }

    // TESTS
    @Test
    public void createReport() {
        ReportCreateDTO reportCreateDTO = new ReportCreateDTO();
        reportCreateDTO.setAgentId(babis.getId());
        reportCreateDTO.setMissionId(transferBabisJrToKrym.getId());
        reportCreateDTO.setMissionResult(MissionResultReportEnum.COMPLETED);
        reportCreateDTO.setText("Make Vodnanske kure great again");
        Long newReportId = reportFacade.createReport(reportCreateDTO);

        ReportDTO newReport = reportFacade.getReportById(newReportId);
        Assert.assertEquals(newReport.getReportStatus(), ReportStatus.NEW);
        Assert.assertEquals(newReport.getText(), reportCreateDTO.getText());
        Assert.assertEquals(newReport.getMissionResult(), reportCreateDTO.getMissionResult());

        // remove newly created report - get to the original state
        reportFacade.deleteReport(newReportId);
    }

    @Test
    public void getReportByIdTestFound() {
        ReportDTO reportDto = reportFacade.getReportById(transferBabisJrToKrymReport.getId());
        Assert.assertNotNull(reportDto);
        Assert.assertEquals(reportDto.getId(), transferBabisJrToKrymReport.getId());
    }

    @Test
    public void getReportByIdTestNotFound() {
        Assert.assertNull(reportFacade.getReportById(555L));
    }

    @Test
    public void getAllReports() {
        List<ReportDTO> reports = reportFacade.getAllReports();
        Assert.assertEquals(reports.size(), 1);
        Assert.assertEquals(reports.get(0).getId(), transferBabisJrToKrymReport.getId());
    }

    @Test
    public void deleteReport() {
        reportFacade.deleteReport(transferBabisJrToKrymReport.getId());
        Assert.assertEquals(reportFacade.getAllReports().size(), 0);

        // save deleted report again
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        transferBabisJrToKrymReport = em.merge(transferBabisJrToKrymReport);
        em.getTransaction().commit();
        em.close();
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = "Entity with given ID doesn't exist")
    public void deleteNonExistingReport() {
        reportFacade.deleteReport(666L);
        Assert.assertEquals(reportFacade.getAllReports().size(), 1);
    }

    @Test
    public void updateReportTextOk() {
        ReportUpdateTextDTO reportUpdateTextDTO = new ReportUpdateTextDTO();
        reportUpdateTextDTO.setId(transferBabisJrToKrymReport.getId());
        reportUpdateTextDTO.setText("Vsecko je to kampan!!!");
        reportFacade.updateReportText(reportUpdateTextDTO);
        Assert.assertEquals(reportFacade.getReportById(transferBabisJrToKrymReport.getId()).getText(),
                "Vsecko je to kampan!!!");
    }

    @Test
    public void approveReportOk() {
        reportFacade.approveReport(transferBabisJrToKrymReport.getId());
        Assert.assertEquals(reportFacade.getReportById(transferBabisJrToKrymReport.getId()).getReportStatus(),
                ReportStatus.APPROVED);

        // set report status back to new
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        transferBabisJrToKrymReport.setReportStatus(ReportStatus.NEW);
        transferBabisJrToKrymReport = em.merge(transferBabisJrToKrymReport);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void denyReportOk() {
        reportFacade.denyReport(transferBabisJrToKrymReport.getId());
        Assert.assertEquals(reportFacade.getReportById(transferBabisJrToKrymReport.getId()).getReportStatus(),
                ReportStatus.DENIED);

        // set report status back to new
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        transferBabisJrToKrymReport.setReportStatus(ReportStatus.NEW);
        transferBabisJrToKrymReport = em.merge(transferBabisJrToKrymReport);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void getReportsFromIntervalBorderValuesOk() {
        LocalDate dateFrom = LocalDate.of(2018, 11, 1);
        LocalDate dateTo = LocalDate.of(2018, 11, 1);

        Assert.assertEquals(reportFacade.getReportsFromInterval(dateFrom, dateTo).size(), 1);
        Assert.assertEquals(reportFacade.getReportsFromInterval(dateFrom, dateTo).get(0).getId(),
                transferBabisJrToKrymReport.getId());
    }

    @Test
    public void getReportsFromIntervalNoReport() {
        LocalDate dateFrom = LocalDate.of(2017, 11, 1);
        LocalDate dateTo = LocalDate.of(2018, 10, 31);

        Assert.assertEquals(reportFacade.getReportsFromInterval(dateFrom, dateTo).size(), 0);
    }

    @Test
    public void getReportsWithResultOk() {
        Assert.assertEquals(reportFacade.getReportsWithResult(MissionResultReportEnum.COMPLETED).size(), 1);
        Assert.assertEquals(reportFacade.getReportsWithResult(MissionResultReportEnum.COMPLETED).get(0).getId(),
                transferBabisJrToKrymReport.getId());
    }

    @Test
    public void getReportsWithStatusOk() {
        Assert.assertEquals(reportFacade.getReportsWithStatus(ReportStatus.NEW).size(), 1);
        Assert.assertEquals(reportFacade.getReportsWithStatus(ReportStatus.NEW).get(0).getId(),
                transferBabisJrToKrymReport.getId());
    }

    @Test
    public void getReportsWithStatusFromMissionOk() {
        List<ReportDTO> reportDTOS = reportFacade.getReportsWithStatusFromMission(ReportStatus.NEW,
                transferBabisJrToKrym.getId());
        Assert.assertEquals(reportDTOS.size(), 1);
        Assert.assertEquals(reportDTOS.get(0).getId(), transferBabisJrToKrymReport.getId());
    }
}
