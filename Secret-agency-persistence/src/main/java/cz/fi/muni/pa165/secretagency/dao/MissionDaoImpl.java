package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MissionDaoImpl extends GenericDaoImpl<Mission> implements MissionDao{
    protected MissionDaoImpl() {
        super(Mission.class);
    }

    @Override
    public List<Mission> getMissionsWithType(MissionTypeEnum type) {
        TypedQuery<Mission> query = em.createQuery(
                "Select m from Mission m where m.missionType = :missionType",
                Mission.class);
        query.setParameter("missionType", type);
        return query.getResultList();
    }

    @Override
    public List<Mission> getMissionsStartedInInterval(LocalDate start, LocalDate end) {
        TypedQuery<Mission> query = em
                .createQuery(
                        "SELECT m FROM Mission m WHERE m.started BETWEEN :startDate AND :endDate",
                        Mission.class);
        query.setParameter("startDate", start);
        query.setParameter("endDate", end);
        return query.getResultList();
    }

    @Override
    public List<Mission> getMissionsInPlace(Double latitude, Double longitude) {
        TypedQuery<Mission> query = em
                .createQuery(
                        "SELECT m FROM Mission m WHERE m.latitude = :latitude AND m.longitude = :longitude ",
                        Mission.class);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        return query.getResultList();
    }

    @Override
    public List<Mission> getActiveMissions() {
        LocalDate now = LocalDate.now();
        TypedQuery<Mission> query = em
                .createQuery(
                        "SELECT m FROM Mission m WHERE m.started <= :now AND m.ended is null ",
                        Mission.class);
        query.setParameter("now", now);
        return query.getResultList();
    }
}
