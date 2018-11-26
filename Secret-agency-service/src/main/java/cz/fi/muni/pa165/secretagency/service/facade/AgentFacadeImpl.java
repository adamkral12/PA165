package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.AgentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.facade.AgentFacade;
import cz.fi.muni.pa165.secretagency.service.AgentService;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.DepartmentService;
import cz.fi.muni.pa165.secretagency.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: Adam Kral <433328>
 * Date: 11/25/18
 * Time: 2:19 PM
 */
@Service
@Transactional
public class AgentFacadeImpl implements AgentFacade {
    @Autowired
    private BeanMappingService beanMappingService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MissionService missionService;

    @Override
    public List<AgentDTO> getAllAgents() {
        return beanMappingService.mapTo(agentService.getAll(), AgentDTO.class);
    }

    @Override
    public AgentDTO getAgentById(Long id) {
        return beanMappingService.mapTo(agentService.getEntityById(id), AgentDTO.class);
    }

    @Override
    public Long createAgent(AgentCreateDTO agentCreateDTO) {
        Agent mappedAgent = beanMappingService.mapTo(agentCreateDTO, Agent.class);

        mappedAgent.setDepartment(departmentService.getEntityById(agentCreateDTO.getDepartmentId()));

        Agent savedAgent = agentService.save(mappedAgent);
        return savedAgent.getId();
    }

    @Override
    public void deleteAgent(Long id) {
        agentService.deleteEntityById(id);
    }

    @Override
    public List<AgentDTO> getAgentsByRank(AgentRankEnum rankEnum) {
        return beanMappingService.mapTo(agentService.getAgentsByRank(rankEnum), AgentDTO.class);
    }

    @Override
    public AgentDTO getAgentByCodeName(String codename) {
        return beanMappingService.mapTo(agentService.getAgentByCodeName(codename), AgentDTO.class);
    }

    @Override
    public void assignAgentToMission(Long agentId, Long missionId) {
        agentService.assignAgentToMission(
                agentService.getEntityById(agentId),
                missionService.getEntityById(missionId)
        );
    }

    @Override
    public void removeAgentFromMission(Long agentId, Long missionId) {
        agentService.removeAgentFromMission(
                agentService.getEntityById(agentId),
                missionService.getEntityById(missionId)
        );
    }

    @Override
    public void addAgentToDepartment(Long agentId, Long departmentId) {
        agentService.addAgentToDepartment(
                agentService.getEntityById(agentId),
                departmentService.getEntityById(departmentId)
        );
    }

    @Override
    public List<AgentDTO> getAgentsWithLanguageKnowledge(LanguageEnum languageEnum) {
        return beanMappingService.mapTo(agentService.getAgentsWithLanguageKnowledge(languageEnum), AgentDTO.class);
    }

    @Override
    public List<AgentDTO> getSoonRetiringAgents() {
        return beanMappingService.mapTo(agentService.getSoonRetiringAgents(), AgentDTO.class);
    }
}
