package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;

import java.util.List;

/**
 * DAO interface that works with agents.
 * @author Milos Silhar (433614)
 */
public interface AgentDao extends GenericDao<Agent> {

    /**
     * Returns all agents who have knowledge of given language.
     * @param language Language to find agents for.
     * @return List of agents with given language skill.
     */
    List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum language);

    /**
     * Returns all agents with given rank.
     * @param rank Rank of agents to search for.
     * @return Agents with given rank.
     */
    List<Agent> getAgentsWithRank(AgentRankEnum rank);

    /**
     * Returns agents with given codename.
     * @param codename Codename of an agent to search for.
     * @return Agents with given codename.
     */
    Agent getAgentByCodename(String codename);

    /**
     * Return agents who are retiring no later than one year from now. Agent is retiring if he/she is more than 60 years old.
     * @return List of retiring agents.
     */
    List<Agent> getSoonRetiringAgents();
}
