package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.AgentDao;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.service.exceptions.AgentServiceException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link AgentService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * Author: Adam Kral <433328>
 * Date: 11/16/18
 * Time: 2:54 PM
 */
@Service
public class AgentServiceImpl extends GenericServiceImpl<Agent, AgentDao> implements AgentService {

    @Override
    public List<Agent> getAgentsByRank(AgentRankEnum rankEnum) {
        if (rankEnum == null) {
            throw new NullPointerException("Rank cannot be null");
        }
        return getDao().getAgentsWithRank(rankEnum);
    }

    @Override
    public Agent getAgentByCodeName(String codename) {
        if (codename == null) {
            throw new NullPointerException("Code name cannot be null");
        }
        return getDao().getAgentByCodename(codename);
    }

    @Override
    public void assignAgentToMission(Agent agent, Mission mission) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (mission == null) {
            throw new NullPointerException("Mission cannot be null");
        }

        if (agent.getMissions().contains(mission)) {
            throw new AgentServiceException("Cannot assign agent to mission, already assigned");
        }
        mission.addAgent(agent);
        getDao().merge(agent);
    }

    @Override
    public void removeAgentFromMission(Agent agent, Mission mission) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (mission == null) {
            throw new NullPointerException("Mission cannot be null");
        }
        if (!agent.getMissions().contains(mission)) {
            throw new AgentServiceException("Cannot remove agent from mission, could not find mission");
        }
        mission.removeAgent(agent);
        getDao().merge(agent);
    }

    @Override
    public void addAgentToDepartment(Agent agent, Department department) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (department == null) {
            throw new NullPointerException("Department cannot be null");
        }
        agent.setDepartment(department);
        getDao().merge(agent);
    }

    @Override
    public List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum language) {
        if (language == null) {
            throw new NullPointerException("Language cannot be null");
        }
        return getDao().getAgentsWithLanguageKnowledge(language);
    }

    @Override
    public List<Agent> getSoonRetiringAgents() {
        return getDao().getSoonRetiringAgents();
    }

    @Override
    public Set<Agent> getAgentsWithCodeNames(Set<String> codeNames) {
        if (codeNames == null) {
            throw new NullPointerException("Codenames must be set");
        }

        if (codeNames.isEmpty()) {
            return new HashSet<>();
        }

        return getDao().getAgentsWithCodeNames(codeNames);
    }
}
