package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.service.exceptions.AgentServiceException;

import java.util.List;
import java.util.Set;

/**
 * An interface that defines a service access to the {@link Agent} entity.
 * @author (Adam Kral<433328>)
 */
public interface AgentService extends GenericService<Agent> {

    /**
     * Try to authenticate a user/agent. Return true only if the hashed password matches the records.
     * @throws NullPointerException when agent is null
     */
    boolean authenticate(Agent agent, String password);

    /**
     * Check if the given agent is admin (his rank must be agent in charge).
     * @throws NullPointerException when agent is null
     */
    boolean isAdmin(Agent agent);

    /**
     * Get all agents by a given rank
     * @param rankEnum given rank
     * @return List of all agents with given rank
     * @throws NullPointerException when rank is null
     */
    List<Agent> getAgentsByRank(AgentRankEnum rankEnum);

    /**
     * update agent
     * @param agent updated
     * @return updated agent
     */
    Agent updateAgent(Agent agent);

    /**
     * Agent with given code name
     * @param codename agent's codename
     * @return agent with given codename
     * @throws NullPointerException when codename is null
     */
    Agent getAgentByCodeName(String codename);

    /**
     * Assigns an agent to mission
     * @param agent agent to be assigned
     * @param mission mission to be assigned on
     * @throws AgentServiceException when agent is already assigned to given mission
     * @throws NullPointerException when Agent or mission is null
     */
    void assignAgentToMission(Agent agent, Mission mission) throws AgentServiceException;

    /**
     * Remove agent from a mission
     * @param agent agent to be removed
     * @param mission mission to be removed from
     * @throws AgentServiceException when given mission could not be found in a list of agent's missions
     * @throws NullPointerException when agent or mission is null
     */
    void removeAgentFromMission(Agent agent, Mission mission) throws AgentServiceException;

    /**
     * Add agent to given department
     * Agent will be removed from current department
     * @param agent to be moved
     * @param department department to be moved to
     * @throws NullPointerException when agent or department is null
     */
    void addAgentToDepartment(Agent agent, Department department);

    /**
     * Get all agents who can speak given language
     * @param languageEnum in which agents can speak
     * @return all agents speaking given language
     * @throws NullPointerException when language is null
     */
    List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum languageEnum);

    /**
     * Returns all agents which are soon to be retired
     * @return all soon retiring agents
     */
    List<Agent> getSoonRetiringAgents();

    /**
     * Returns agents with selected codeNames
     * @param codeNames set of selected codeNames
     * @return set of agent with selected codeNames or empty set, when parameter is empty or no agent is found
     * @throws NullPointerException when codeNames is not set
     */
    Set<Agent> getAgentsWithCodeNames(Set<String> codeNames);
}
