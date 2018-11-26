package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import cz.fi.muni.pa165.secretagency.service.exceptions.ReportServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * An interface that defines a service access to the {@link Report} entity.
 *
 * @author Jan Pavlu (487548)
 */
public interface ReportService extends GenericService<Report> {
    /**
     * Get all reports in given interval
     * @param dateFrom bottom border of interval
     * @param dateTo top border of interval
     * @return all reports within interval
     * @throws NullPointerException when any of parameters is null
     * @throws ReportServiceException when date from is above date to
     */
    List<Report> getReportsFromInterval(LocalDate dateFrom, LocalDate dateTo);

    /**
     * Get all reports with specific mission result report
     * @param missionResultReport mission result report
     * @return all reports with specified mission result report
     * @throws NullPointerException when missionResultReport is null
     */
    List<Report> getReportsWithResult(MissionResultReportEnum missionResultReport);

    /**
     * Get all reports with specified report status
     * @param reportStatus report status
     * @return all reports with specified report status
     * @throws NullPointerException when reportStatus is null
     */
    List<Report> getReportsWithStatus(ReportStatus reportStatus);

    /**
     * Get all reports with specified status from the mission
     * @param reportStatus report status
     * @param mission mission
     * @return all reports with specified status from the mission
     * @throws NullPointerException when any of parameters is null
     */
    List<Report> getReportsWithStatusFromMission(ReportStatus reportStatus, Mission mission);

    /**
     * Updates text in selected report.
     * @param report updated data
     * @param text new text
     */
    void updateReportText(Report report, String text);

    /**
     * Updates report status
     * @param report report
     * @param reportStatus new status
     */
    void changeReportStatus(Report report, ReportStatus reportStatus);

    /**
     * Returns set of agents mentioned in report. Agents codename must be prefixed with word "agent" in the text
     *   to be recognized. For example "agent Black" is recognized, if Black is agents codename, but plain "black" is
     *   not. This rule helps to avoid fake matches.
     *
     * @param reportId id of the report
     * @return set of matched agents or empty set
     * @throws NullPointerException when reportId is not set
     * @throws ReportServiceException when report with given id does not exist
     * @throws ReportServiceException when database does not contain any agents
     */
    Set<Agent> getAgentsMentionedInReport(Long reportId);
}
