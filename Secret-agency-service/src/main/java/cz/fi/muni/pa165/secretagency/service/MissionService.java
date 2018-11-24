package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.service.exceptions.MissionServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for service access to the {@link Mission} entity.
 *
 * @author Adam Skurla (487588)
 */
public interface MissionService extends GenericService<Mission> {
    /**
     * Gets all missions by given type
     * @param type to filter by
     * @return list of mission with type
     * @throws NullPointerException when type parameter is null
     */
    List<Mission> getMissionsWithType(MissionTypeEnum type);

    /**
     * Gets all missions which happened in given interval
     * @param start of interval
     * @param end of interval
     * @return list of mission between interval
     * @throws NullPointerException when any of parameters is null
     * @throws MissionServiceException when start is after end
     */
    List<Mission> getMissionsStartedInInterval(LocalDate start, LocalDate end);

    /**
     * Returns all missions at a given place
     * @param latitude of place
     * @param longitude of place
     * @return all missions at given place
     * @throws NullPointerException when any of parameters is null
     */
    List<Mission> getMissionsInPlace(Double latitude, Double longitude);

    /**
     * Return list of all missions which are currently active
     * @return currently active missions
     */
    List<Mission> getActiveMissions();
}
