package cz.fi.muni.pa165.secretagency.facade;

import cz.fi.muni.pa165.secretagency.dto.AgentAuthenticateDTO;
import cz.fi.muni.pa165.secretagency.dto.AgentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;

import java.util.List;

/**
 * Author: Adam Kral <433328>
 * Date: 11/25/18
 * Time: 1:05 PM
 */
public interface AgentFacade {
    /**
     * List of all agents
     * @return list of all agents
     */
    List<AgentDTO> getAllAgents();

    /**
     * Get agent by id
     * @param id id of agent
     * @return agent with given id
     */
    AgentDTO getAgentById(Long id);

    /**
     * create new agent
     * @param agentCreateDTO agent to be created
     * @return created agent's id
     */
    Long createAgent(AgentCreateDTO agentCreateDTO);

    /**
     * Delete agent by given id
     * @param id if agent to be deleted
     */
    void deleteAgent(Long id);

    /**
     * Get all ranks
     * @return all ranks
     */
    AgentRankEnum[] getAgentRanks();

    /**
     * Get all agents by a given rank
     * @param rankEnum given rank
     * @return List of all agents with given rank
     */
    List<AgentDTO> getAgentsByRank(AgentRankEnum rankEnum);

    /**
     * List of all agents with given code name
     * @param codename agent's codename
     * @return agents with given codename
     * @throws NullPointerException when with given id does not exist
     */
    AgentDTO getAgentByCodeName(String codename);

    /**
     * Assigns an agent to mission
     * @param agentId agent to be assigned
     * @param missionId mission to be assigned on
     */
    void assignAgentToMission(Long agentId, Long missionId);

    /**
     * Remove agent from a mission
     * @param agentId agent to be removed
     * @param missionId mission to be removed from
     */
    void removeAgentFromMission(Long agentId, Long missionId);

    /**
     * Add agent to given department
     * Agent will be removed from current department
     * @param agentId to be moved
     * @param departmentId department to be moved to
     */
    void addAgentToDepartment(Long agentId, Long departmentId);

    /**
     * Get all agents who can speak given language
     * @param languageEnum in which agents can speak
     * @return all agents speaking given language
     */
    List<AgentDTO> getAgentsWithLanguageKnowledge(LanguageEnum languageEnum);

    /**
     * Returns all agents which are soon to be retired
     * @return all soon retiring agents
     */
    List<AgentDTO> getSoonRetiringAgents();

    /**
     * Try to authenticate an agent. Return true only if the hashed password matches the records.
     */
    boolean authenticate(AgentAuthenticateDTO agent);

    /**
     * Check if the given agent is admin (his rank is agent in charge).
     */
    boolean isAdmin(Long agentId);
}
