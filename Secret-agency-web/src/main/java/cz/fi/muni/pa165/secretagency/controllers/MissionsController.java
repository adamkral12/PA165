package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import cz.fi.muni.pa165.secretagency.dto.MissionCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.MissionDTO;
import cz.fi.muni.pa165.secretagency.dto.MissionUpdateDTO;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.exceptions.InvalidMissionNameException;
import cz.fi.muni.pa165.secretagency.exceptions.ResourceNotFoundException;
import cz.fi.muni.pa165.secretagency.facade.AgentFacade;
import cz.fi.muni.pa165.secretagency.facade.MissionFacade;
import cz.fi.muni.pa165.secretagency.facade.ReportFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * REST controller for missions.
 *
 * @author Milos Silhar (433614)
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_REST + ApiUris.ROOT_URI_MISSIONS)
public class MissionsController {
    final  static Logger logger = LoggerFactory.getLogger(MissionsController.class);

    @Autowired
    private MissionFacade missionFacade;
    @Autowired
    private ReportFacade reportFacade;
    @Autowired
    private AgentFacade agentFacade;

    /**
     * Get list of all missions
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions
     *
     * @return List of all missions
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionDTO> getMissions() {
        logger.debug("rest get all missions");
        return missionFacade.getAllMissions();
    }

    /**
     * Get list of all active missions
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/active
     *
     * @return List of all active missions
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionDTO> getActiveMissions() {
        logger.debug("rest get all missions");
        return missionFacade.getActiveMissions();
    }

    /**
     * Get mission with given id
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/{id}
     *
     * @param id Identifier for mission
     * @return MissionDTO
     * @throws ResourceNotFoundException When mission with given id is not found
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final MissionDTO getMission(@PathVariable("id") Long id) throws ResourceNotFoundException {
        logger.debug("rest get mission with id: {}", id);

        try {
            MissionDTO mission = missionFacade.getMissionById(id);
            return mission;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Get missions in specific place
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/place/{latitude}/{longitude}
     *
     * @param latitude Latitude of the place
     * @param longitude Longitude of the place
     * @return List of missions in place
     */
    @RequestMapping(value = "/place/{latitude}/{longitude}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionDTO> getMissionsInPlace(@PathVariable("latitude") Double latitude, @PathVariable("longitude") Double longitude) {
        logger.debug("rest get missions in place [{}, {}]", latitude, longitude);

        return missionFacade.getMissionsInPlace(latitude, longitude);
    }

    /**
     * Get missions from specific time period
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/interval/{from:yyyy-MM-dd}/{to:yyyy-MM-dd}
     *
     * @param dateFrom Start of the time period, in format yyyy-MM-dd
     * @param dateTo End of the time period, in format yyyy-MM-dd
     * @return List of missions in time period
     */
    @RequestMapping(value = "/interval/{dateFrom}/{dateTo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionDTO> getMissionsStartedInInterval(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dateFrom,
                                                  @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dateTo) {
        logger.debug("rest get missions in date interval [{}, {}]", dateFrom, dateTo);

        return missionFacade.getMissionsStartedInInterval(dateFrom, dateTo);
    }

    /**
     * Get all types of missions
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/types
     *
     * @return List of all types of missions
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionTypeEnum> getMissionTypes() {
        logger.debug("rest get mission types");

        return Arrays.asList(MissionTypeEnum.values());
    }

    /**
     * Get all missions with specific type
     * curl --cookie {COOKIE_VALUE} -i -X GET http://localhost:8080/pa165/rest/missions/type/{missionType}
     *
     * @param missionTypeEnum Type of mission
     * @return List of all missions with type
     */
    @RequestMapping(value = "/type/{missionType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<MissionDTO> getMissionsWithType(@PathVariable("missionType") MissionTypeEnum missionTypeEnum) {
        logger.debug("rest get missions with type {}", missionTypeEnum);

        return missionFacade.getMissionsWithType(missionTypeEnum);
    }

    /**
     * Creates new mission
     * curl --cookie "{COOKIE_VALUE}" -X POST -i -H "Content-Type: application/json" --data
     * '{ "latitude": "23.52", "longitude": "15.22", "missionType": "SABOTAGE", "started":"2013-02-11"}'
     * http://localhost:8080/pa165/rest/missions
     *
     * @param missionCreateDTO MissionCreateDTO with required fields for creation
     * @return The created mission MissionDTO
     * @throws InvalidMissionNameException When mission with given name already exists
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final MissionDTO createMission(@Valid @RequestBody MissionCreateDTO missionCreateDTO) {
        logger.debug("rest create mission");
        MissionDTO existingMission = missionFacade.getMissionByName(missionCreateDTO.getName());
        if (existingMission != null) {
            throw new InvalidMissionNameException();
        }
        Long missionId = missionFacade.createMission(missionCreateDTO);
        return missionFacade.getMissionById(missionId);
    }

    /**
     * Updates mission
     * curl --cookie "{COOKIE_VALUE}" -X PUT -i -H "Content-Type: application/json" --data
     * '{ "latitude": "23.52", "longitude": "15.22", "missionType": "SABOTAGE", "started":"2013-02-11", "ended":"2014-10-12"}'
     * http://localhost:8080/pa165/rest/missions/2
     *
     * @param id Identifier for mission
     * @param missionUpdateDTO MissionUpdateDTO with required fields for creation
     * @return The created mission MissionDTO
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final MissionDTO updateMission(@PathVariable("id") Long id, @Valid @RequestBody MissionUpdateDTO missionUpdateDTO) {
        logger.debug("rest update mission with id: {}", id);

        MissionDTO existingMissionName = missionFacade.getMissionByName(missionUpdateDTO.getName());
//            Check if different mission with this name already exists
        if (existingMissionName != null) {
            if(!existingMissionName.getId().equals(id)) {
                throw new InvalidMissionNameException();
            }
        }

        try {
            missionUpdateDTO.setId(id);
            missionFacade.updateMission(missionUpdateDTO);

            return missionFacade.getMissionById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Assigns agent to mission
     * curl --cookie {COOKIE_VALUE} -i -X PUT http://localhost:8080/pa165/rest/missions/{agentId}/{missionId}
     *
     * @param agentId Identifier for agent
     * @param missionId Identifier for mission
     */
    @RequestMapping(value = "/{agentId}/{missionId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void assignAgentToMission(@PathVariable("agentId") Long agentId, @PathVariable("missionId") Long missionId) {
        logger.debug("rest assign agent id: {} to mission id: {}", agentId, missionId);

        try {
            agentFacade.assignAgentToMission(agentId, missionId);
        }
        catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Delete one mission
     * curl --cookie {COOKIE_VALUE} -i -X DELETE http://localhost:8080/pa165/rest/missions/{id}
     *
     * @param id Identifier for mission
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteMission(@PathVariable("id") Long id) {
        logger.debug("rest delete mission with id: {}", id);

        try {
            MissionDTO missionDTO = missionFacade.getMissionById(id);
            missionDTO.getAgentIds().stream().forEach((agentId) ->
                    agentFacade.removeAgentFromMission(agentId, missionDTO.getId()));
            missionDTO.getReportIds().stream().forEach((reportId) ->
                    reportFacade.deleteReport(reportId));
            missionFacade.deleteMission(id);
        }
        catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Remove agent from mission
     * curl --cookie {COOKIE_VALUE} -i -X DELETE http://localhost:8080/pa165/rest/missions/{agentId}/{missionId}
     *
     * @param agentId Identifier for agent
     * @param missionId Identifier for mission
     */
    @RequestMapping(value = "/{agentId}/{missionId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void removeAgentFromMission(@PathVariable("agentId") Long agentId, @PathVariable("missionId") Long missionId) {
        logger.debug("rest remove agent id: {} from mission id: {}", agentId, missionId);

        try {
            agentFacade.removeAgentFromMission(agentId, missionId);
        }
        catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException();
        }
    }
}
