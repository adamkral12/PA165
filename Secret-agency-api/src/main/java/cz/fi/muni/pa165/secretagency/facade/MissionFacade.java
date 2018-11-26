package cz.fi.muni.pa165.secretagency.facade;

import cz.fi.muni.pa165.secretagency.dto.MissionCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.MissionDTO;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Mission facade.
 * @author Adam Skurla (487588)
 */
public interface MissionFacade {

    /**
     * Return all missions.
     * @return all missions.
     */
    List<MissionDTO> getAllMissions();

    /**
     * Get mission by id.
     * @param id mission id
     */
    MissionDTO getMissionById(Long id);

    /**
     * Create new mission.
     * @param mission to create
     */
    Long createMission(MissionCreateDTO mission);

    /**
     * Delete mission.
     * @param missionId of mission for deletion
     */
    void deleteMission(Long missionId);

    /**
     * Gets all missions by given type
     * @param type to filter by
     * @return list of mission with type
     */
    List<MissionDTO> getMissionsWithType(MissionTypeEnum type);

    /**
     * Gets all missions which happened in given interval
     * @param start of interval
     * @param end of interval
     * @return list of mission between interval
     */
    List<MissionDTO> getMissionsStartedInInterval(LocalDate start, LocalDate end);

    /**
     * Returns all missions at a given place
     * @param latitude of place
     * @param longitude of place
     * @return all missions at given place
     */
    List<MissionDTO> getMissionsInPlace(Double latitude, Double longitude);

    /**
     * Return list of all missions which are currently active
     * @return currently active missions
     */
    List<MissionDTO> getActiveMissions();
}
