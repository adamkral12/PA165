package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportUpdateTextDTO;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.*;
import cz.fi.muni.pa165.secretagency.facade.ReportFacade;
import cz.fi.muni.pa165.secretagency.service.AgentService;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.MissionService;
import cz.fi.muni.pa165.secretagency.service.ReportService;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for report Facade.
 *
 * @author Jan Pavlu
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReportFacadeUnitTest extends AbstractTestNGSpringContextTests {

    @Mock
    private ReportService reportService;

    @Mock
    private MissionService missionService;

    @Mock
    private AgentService agentService;

    @Autowired
    private ReportFacade reportFacade;

    @Autowired
    private BeanMappingService beanMappingService;

    private Report transferBabisJrToKrymReport;
    private Report makeVodnanskeKureGreatAgainReport;
    private Mission transferBabisJrToKrym;
    private Agent babis;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(reportFacade, "reportService", reportService);
        ReflectionTestUtils.setField(reportFacade, "missionService", missionService);
        ReflectionTestUtils.setField(reportFacade, "agentService", agentService);

        setUpReports();
        setUpMission();
        setUpAgent();
    }

    private void setUpReports() {
        transferBabisJrToKrymReport = new Report();
        transferBabisJrToKrymReport.setId(1L);
        transferBabisJrToKrymReport.setText("Get rid of my son");
        transferBabisJrToKrymReport.setReportStatus(ReportStatus.NEW);
        transferBabisJrToKrymReport.setDate(LocalDate.of(2018, 11, 1));
        transferBabisJrToKrymReport.setMissionResult(MissionResultReportEnum.COMPLETED);

        makeVodnanskeKureGreatAgainReport = new Report();
        makeVodnanskeKureGreatAgainReport.setId(10L);
        makeVodnanskeKureGreatAgainReport.setText("Make Vodnanske kure great again");
        makeVodnanskeKureGreatAgainReport.setReportStatus(ReportStatus.DENIED);
        makeVodnanskeKureGreatAgainReport.setDate(LocalDate.of(2058, 1, 1));
        makeVodnanskeKureGreatAgainReport.setMissionResult(MissionResultReportEnum.FAILED);
    }

    private void setUpMission() {
        transferBabisJrToKrym = new Mission();
        transferBabisJrToKrym.setId(666L);
        transferBabisJrToKrym.setMissionType(MissionTypeEnum.SABOTAGE);
        transferBabisJrToKrym.setStarted(LocalDate.of(2017, 5, 1));
        transferBabisJrToKrym.setEnded(LocalDate.of(2018, 11, 1));
        transferBabisJrToKrym.setLatitude(47.304324);
        transferBabisJrToKrym.setLongitude(39.521161);
    }

    private void setUpAgent() {
        babis = new Agent();
        babis.setId(42L);
        babis.setLanguages(Collections.singleton(LanguageEnum.SK));
        babis.setRank(AgentRankEnum.AGENT_IN_CHARGE);
        babis.setName("Andrej Babis");
        babis.setCodeName("Bures");
        babis.setBirthDate(LocalDate.of(1980, 8, 8));
    }

    @Test
    public void getAllReports() {
        when(reportService.getAll()).thenReturn(Arrays.asList(transferBabisJrToKrymReport,
                makeVodnanskeKureGreatAgainReport));
        List<ReportDTO> reportDTOS = reportFacade.getAllReports();
        Assert.assertEquals(reportDTOS.size(), 2);
        Assert.assertTrue(reportDTOS.contains(beanMappingService.mapTo(transferBabisJrToKrymReport, ReportDTO.class)));
        Assert.assertTrue(reportDTOS.contains(beanMappingService.mapTo(makeVodnanskeKureGreatAgainReport,
                ReportDTO.class)));
    }

    @Test
    public void getReportById() {
        when(reportService.getEntityById(transferBabisJrToKrymReport.getId())).thenReturn(transferBabisJrToKrymReport);
        Assert.assertEquals(reportFacade.getReportById(transferBabisJrToKrymReport.getId()),
                beanMappingService.mapTo(transferBabisJrToKrymReport, ReportDTO.class));
    }

    @Test
    public void createReport() {
        ReportCreateDTO reportCreateDTO = new ReportCreateDTO();
        reportCreateDTO.setText("Whore for the pictures with Babis junior was found");
        reportCreateDTO.setMissionResult(MissionResultReportEnum.COMPLETED);
        reportCreateDTO.setMissionId(transferBabisJrToKrym.getId());
        reportCreateDTO.setAgentId(babis.getId());

        when(agentService.getEntityById(babis.getId())).thenReturn(babis);
        when(missionService.getEntityById(transferBabisJrToKrym.getId())).thenReturn(transferBabisJrToKrym);
        when(reportService.save(any())).thenReturn(transferBabisJrToKrymReport);
        reportFacade.createReport(reportCreateDTO);
        verify(reportService, times(1)).save(any());
    }

    @Test
    public void deleteReport() {
        reportFacade.deleteReport(transferBabisJrToKrymReport.getId());
        verify(reportService).deleteEntityById(transferBabisJrToKrymReport.getId());
    }

    @Test
    public void updateReportText() {
        ReportUpdateTextDTO reportUpdateTextDTO = new ReportUpdateTextDTO();
        reportUpdateTextDTO.setId(transferBabisJrToKrymReport.getId());
        reportUpdateTextDTO.setText("Bakalovi novinari, vsecko je kampan, Kalousek");
        when(reportService.getEntityById(transferBabisJrToKrymReport.getId())).thenReturn(transferBabisJrToKrymReport);
        reportFacade.updateReportText(reportUpdateTextDTO);
        verify(reportService, times(1)).updateReportText(transferBabisJrToKrymReport,
                "Bakalovi novinari, vsecko je kampan, Kalousek");
    }

    @Test
    public void approveReport() {
        when(reportService.getEntityById(transferBabisJrToKrymReport.getId())).thenReturn(transferBabisJrToKrymReport);
        reportFacade.approveReport(transferBabisJrToKrymReport.getId());
        verify(reportService, times(1)).changeReportStatus(transferBabisJrToKrymReport,
                ReportStatus.APPROVED);
    }

    @Test
    public void denyReport() {
        when(reportService.getEntityById(transferBabisJrToKrymReport.getId())).thenReturn(transferBabisJrToKrymReport);
        reportFacade.denyReport(transferBabisJrToKrymReport.getId());
        verify(reportService, times(1)).changeReportStatus(transferBabisJrToKrymReport,
                ReportStatus.DENIED);
    }

    @Test
    public void getReportsFromInterval() {
        LocalDate dateFrom = LocalDate.of(2017, 1, 1);
        LocalDate dateTo = LocalDate.of(2019, 1, 1);

        when(reportService.getReportsFromInterval(dateFrom, dateTo)).thenReturn(
                Collections.singletonList(transferBabisJrToKrymReport));
        Assert.assertEquals(reportFacade.getReportsFromInterval(dateFrom, dateTo).size(), 1);
        Assert.assertEquals(reportFacade.getReportsFromInterval(dateFrom, dateTo).get(0),
                beanMappingService.mapTo(transferBabisJrToKrymReport, ReportDTO.class));
    }

    @Test
    public void getReportsWithResult() {
        when(reportService.getReportsWithResult(MissionResultReportEnum.FAILED)).thenReturn(
                Collections.singletonList(makeVodnanskeKureGreatAgainReport));
        Assert.assertEquals(reportFacade.getReportsWithResult(MissionResultReportEnum.FAILED).size(), 1);
        Assert.assertEquals(reportFacade.getReportsWithResult(MissionResultReportEnum.FAILED).get(0),
                beanMappingService.mapTo(makeVodnanskeKureGreatAgainReport, ReportDTO.class));
    }

    @Test
    public void getReportsWithStatus() {
        when(reportService.getReportsWithStatus(ReportStatus.APPROVED)).thenReturn(
                Collections.singletonList(transferBabisJrToKrymReport));
        Assert.assertEquals(reportFacade.getReportsWithStatus(ReportStatus.APPROVED).size(), 1);
        Assert.assertEquals(reportFacade.getReportsWithStatus(ReportStatus.APPROVED).get(0),
                beanMappingService.mapTo(transferBabisJrToKrymReport, ReportDTO.class));
    }

    @Test
    public void getReportsWithStatusFromMission() {
        when(reportService.getReportsWithStatusFromMission(ReportStatus.APPROVED, transferBabisJrToKrym)).thenReturn(
                Collections.singletonList(transferBabisJrToKrymReport));
        when(missionService.getEntityById(transferBabisJrToKrym.getId())).thenReturn(transferBabisJrToKrym);
        List<ReportDTO> reportsWithStatusFromMission = reportFacade.getReportsWithStatusFromMission(ReportStatus.APPROVED,
                transferBabisJrToKrym.getId());
        Assert.assertEquals(reportsWithStatusFromMission.size(), 1);
        Assert.assertEquals(reportsWithStatusFromMission.get(0).getId(), transferBabisJrToKrymReport.getId());
    }

    @Test
    public void getAgentsMentionedInReport() {
        Set<Agent> mentionedAgents = new HashSet<>(Arrays.asList(babis));
        when(reportService.getAgentsMentionedInReport(transferBabisJrToKrymReport.getId())).thenReturn(mentionedAgents);
        Assert.assertEquals(reportFacade.getAgentsMentionedInReport(transferBabisJrToKrymReport.getId()),
                beanMappingService.mapToSet(mentionedAgents, AgentDTO.class));
    }

}
