package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Adam Kral (433328)
 */
public interface MissionDao extends GenericDao<Mission> {
    /**
     * Gets all missions by given type
     * @param type to filter by
     * @return list of mission with type
     */
    List<Mission> getMissionsWithType(MissionTypeEnum type);

    /**
     * Gets all missions which happened in given interval
     * @param start of interval
     * @param end of interval
     * @return list of mission between interval
     */
    List<Mission> getMissionsStartedInInterval(LocalDate start, LocalDate end);

    /**
     * Returns all missions at a given place
     * @param latitude of place
     * @param longitude of place
     * @return all missions at given place
     */
    List<Mission> getMissionsInPlace(Double latitude, Double longitude);

    /**
     * Return list of all missions which are currently active
     * @return currently active missions
     */
    List<Mission> getActiveMissions();
}
