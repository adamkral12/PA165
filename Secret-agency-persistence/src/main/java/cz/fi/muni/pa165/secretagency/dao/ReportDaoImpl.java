package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of DAO for the Report entity.
 *
 * @author Adam Skurla (487588)
 */
@Repository
public class ReportDaoImpl extends GenericDaoImpl<Report> implements ReportDao {

    public ReportDaoImpl() {
        super(Report.class);
    }

    @Override
    public List<Report> getReportsFromInterval(LocalDate dateFrom, LocalDate dateTo) {
        TypedQuery<Report> query = em.createQuery("select r from Report r where r.date between :dateFrom and :dateTo", Report.class);
        return query
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
    }

    @Override
    public List<Report> getReportsWithResult(MissionResultReportEnum missionResultReport) {
        TypedQuery<Report> query = em.createQuery("select r from Report r where r.missionResult = :resultReport", Report.class);
        return query
                .setParameter("resultReport", missionResultReport)
                .getResultList();
    }

    @Override
    public List<Report> getReportsWithStatus(ReportStatus reportStatus) {
        TypedQuery<Report> query = em.createQuery("select r from Report r where r.reportStatus = :reportStatus", Report.class);
        return query
                .setParameter("reportStatus", reportStatus)
                .getResultList();
    }

    @Override
    public List<Report> getReportsWithStatusFromMission(ReportStatus reportStatus, Mission mission) {
        TypedQuery<Report> query = em.createQuery("select r from Report r where r.reportStatus = :reportStatus and r.mission = :mission", Report.class);
        return query
                .setParameter("reportStatus", reportStatus)
                .setParameter("mission", mission)
                .getResultList();
    }
}
