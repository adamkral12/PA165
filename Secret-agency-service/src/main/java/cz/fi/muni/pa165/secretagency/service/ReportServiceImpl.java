package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.ReportDao;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import cz.fi.muni.pa165.secretagency.service.exceptions.ReportServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the {@link ReportService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * @author Jan Pavlu
 */
@Service
@Transactional
public class ReportServiceImpl extends GenericServiceImpl<Report, ReportDao> implements ReportService {

    @Override
    public List<Report> getReportsFromInterval(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new NullPointerException("Date from and date to must be set.");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new ReportServiceException("Date from cannot have higher value than date to");
        }
        return getDao().getReportsFromInterval(dateFrom, dateTo);
    }

    @Override
    public List<Report> getReportsWithResult(MissionResultReportEnum missionResultReport) {
        if (missionResultReport == null) {
            throw new NullPointerException("Mission result must be set");
        }
        return getDao().getReportsWithResult(missionResultReport);
    }

    @Override
    public List<Report> getReportsWithStatus(ReportStatus reportStatus) {
        if (reportStatus == null) {
            throw new NullPointerException("Report status must be set");
        }
        return getDao().getReportsWithStatus(reportStatus);
    }

    @Override
    public List<Report> getReportsWithStatusFromMission(ReportStatus reportStatus, Mission mission) {
        if (reportStatus == null) {
            throw new NullPointerException("Report status must be set");
        }

        if (mission == null) {
            throw new NullPointerException("Mission must be set");
        }
        return getDao().getReportsWithStatusFromMission(reportStatus, mission);
    }
}
