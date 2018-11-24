package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.ReportDao;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import cz.fi.muni.pa165.secretagency.service.exceptions.ReportServiceException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;


/**
 * Tests for report service class.
 *
 * @author Jan Pavlu (487548)
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class ReportServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    private ReportDao reportDao;

    @Autowired
    private ReportService reportService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(reportService, "dao", reportDao);
    }

    /*****************************************************
     *  GET REPORTS FROM INTERVAL
     ****************************************************/
    @Test
    public void getReportsFromIntervalOk() {
        Report report_january_1 = new Report();
        LocalDate report_from = LocalDate.of(1990, 1, 1);
        report_january_1.setId(1L);
        report_january_1.setDate(report_from);

        Report report_january_2 = new Report();
        LocalDate report_to = LocalDate.of(1990, 12, 31);
        report_january_2.setId(2L);
        report_january_2.setDate(report_to);

        List<Report> reportsFromJanuary = Arrays.asList(report_january_1, report_january_2);

        when(reportDao.getReportsFromInterval(report_from, report_to)).thenReturn(reportsFromJanuary);
        List<Report> reportsFromInterval = reportService.getReportsFromInterval(report_from, report_to);

        // test size
        Assert.assertEquals(reportsFromInterval.size(), reportsFromJanuary.size());

        // test, that list contains expected elements (based on id)
        Assert.assertEquals(reportsFromInterval.stream().filter(report -> report.getId().equals(1L))
                                                        .count(), 1);
        Assert.assertEquals(reportsFromInterval.stream().filter(report -> report.getId().equals(2L))
                                                        .count(), 1);
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Date from and date to must be set.")
    public void getReportsFromIntervalDateFromNotSet() {
        reportService.getReportsFromInterval(null, LocalDate.of(2018, 1, 1));
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Date from and date to must be set.")
    public void getReportsFromIntervalDateToNotSet() {
        reportService.getReportsFromInterval(LocalDate.of(2018, 1, 1), null);
    }

    @Test(expectedExceptions = ReportServiceException.class,
          expectedExceptionsMessageRegExp = "Date from cannot have higher value than date to")
    public void getReportsFromIntervalDateToHigherThanDateFrom() {
        reportService.getReportsFromInterval(LocalDate.of(2018, 12, 30),
                LocalDate.of(2018, 1, 1));
    }

    /*****************************************************
     *  GET REPORTS WITH RESULT
     ****************************************************/
    @Test
    public void getReportsWithResultOk() {
        Report successful_mission = new Report();
        successful_mission.setId(50L);
        successful_mission.setMissionResult(MissionResultReportEnum.COMPLETED);

        Report failed_mission = new Report();
        failed_mission.setId(666L);
        failed_mission.setMissionResult(MissionResultReportEnum.FAILED);

        when(reportDao.getReportsWithResult(MissionResultReportEnum.COMPLETED)).thenReturn(
                Collections.singletonList(successful_mission));
        List<Report> successfulReports = reportService.getReportsWithResult(MissionResultReportEnum.COMPLETED);
        Assert.assertEquals(successfulReports.size(), 1);
        Assert.assertEquals(successfulReports.get(0).getId(), (Long) 50L);
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Mission result must be set")
    public void getReportsWithResultResultNotSet() {
        reportService.getReportsWithResult(null);
    }

    /*****************************************************
     *  GET REPORTS WITH STATUS
     ****************************************************/
    @Test
    public void getReportsWithStatusOk() {
        Report approvedReport = new Report();
        approvedReport.setId(33L);
        approvedReport.setReportStatus(ReportStatus.APPROVED);

        when(reportDao.getReportsWithStatus(ReportStatus.APPROVED)).thenReturn(
                Collections.singletonList(approvedReport));
        when(reportDao.getReportsWithStatus(ReportStatus.DENIED)).thenReturn(Collections.emptyList());

        // no report exists
        List<Report> deniedReports = reportService.getReportsWithStatus(ReportStatus.DENIED);
        Assert.assertEquals(deniedReports.size(), 0L);

        // one report exists
        List<Report> approvedReports = reportService.getReportsWithStatus(ReportStatus.APPROVED);
        Assert.assertEquals(approvedReports.size(), 1L);
        Assert.assertEquals(approvedReports.get(0).getId(), (Long) 33L);
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Report status must be set")
    public void getReportsWithStatusNotSet() {
        reportService.getReportsWithStatus(null);
    }

    /*****************************************************
     *  GET REPORTS WITH STATUS
     ****************************************************/
    @Test
    public void getReportsWithStatusFromMissionOk() {
        Mission assassination = new Mission();
        assassination.setId(1L);
        assassination.setLongitude(2.5);
        assassination.setLatitude(2.7);
        assassination.setStarted(LocalDate.now());
        assassination.setMissionType(MissionTypeEnum.ASSASSINATION);
        assassination.setEnded(LocalDate.now());

        Report approvedReportFromAssassination = new Report();
        approvedReportFromAssassination.setId(10L);
        approvedReportFromAssassination.setMissionResult(MissionResultReportEnum.COMPLETED);
        approvedReportFromAssassination.setMission(assassination);

        when(reportDao.getReportsWithStatusFromMission(ReportStatus.APPROVED, assassination)).thenReturn(
                Collections.singletonList(approvedReportFromAssassination));
        List<Report> approvedReportsFromAssassination = reportService.getReportsWithStatusFromMission(
                ReportStatus.APPROVED, assassination);

        Assert.assertEquals(approvedReportsFromAssassination.size(), 1L);
        Assert.assertEquals(approvedReportsFromAssassination.get(0).getId(), (Long) 10L);
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Report status must be set")
    public void getReportsWithStatusFromMissionStatusNotSet() {
        reportService.getReportsWithStatusFromMission(null, new Mission());
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "Mission must be set")
    public void getReportsWithStatusFromMissionMissionNotSet() {
        reportService.getReportsWithStatusFromMission(ReportStatus.APPROVED, null);
    }
}
