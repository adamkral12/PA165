package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Adam Skurla (487588)
 */
public interface ReportDao extends GenericDao<Report> {
    /**
     * Get all reports in given interval
     * @param dateFrom bottom border of interval
     * @param dateTo top border of interval
     * @return all reports within interval
     */
    List<Report> getReportsFromInterval(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Get all reports with specific mission result report
     * @param missionResultReport mission result report
     * @return all reports with specified mission result report
     */
    List<Report> getReportsWithResult(MissionResultReportEnum missionResultReport);

    /**
     * Get all reports with specified report status
     * @param reportStatus report status
     * @return all reports with specified report status
     */
    List<Report> getReportsWithStatus(ReportStatus reportStatus);

    /**
     * Get all reports with specified status from the mission
     * @param reportStatus report status
     * @param mission mission
     * @return all reports with specified status from the mission
     */
    List<Report> getReportsWithStatusFromMission(ReportStatus reportStatus, Mission mission);
}
