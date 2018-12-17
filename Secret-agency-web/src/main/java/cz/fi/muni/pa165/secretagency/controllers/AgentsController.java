package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.dto.AgentUpdateDTO;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.exceptions.ResourceNotFoundException;
import cz.fi.muni.pa165.secretagency.facade.AgentFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Adam Skurla (487588)
 * Agents Rest Controller
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_REST + ApiUris.ROOT_URI_AGENTS)
public class AgentsController {
    final static Logger logger = LoggerFactory.getLogger(AgentsController.class);

    @Autowired
    private AgentFacade agentFacade;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<AgentDTO> getAgents() {
        logger.debug("rest getAgents()");
        return this.agentFacade.getAllAgents();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final AgentDTO getAgent(@PathVariable("id") Long id) throws ResourceNotFoundException {
        logger.debug("rest getAgent({})", id);
        try {
            return agentFacade.getAgentById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final AgentDTO updateAgent(
            @PathVariable("id") Long id,
            @RequestBody AgentUpdateDTO agentUpdateDTO
    ) throws ResourceNotFoundException {
        logger.debug("rest update agent ({})", agentUpdateDTO);
        try {
            agentFacade.editAgent(agentUpdateDTO);
            return agentFacade.getAgentById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/ranks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final AgentRankEnum[] getAgentRanks() {
        logger.debug("rest getAgentRanks()");
        return agentFacade.getAgentRanks();
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final LanguageEnum[] getAllLanguages() {
        logger.debug("rest getAllLanguages()");
        return agentFacade.getAllLanguages();
    }

    @RequestMapping(value = "/rank/{rank}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<AgentDTO> getAgentsByRank(@PathVariable("rank") AgentRankEnum agentRank) {
        logger.debug("rest get agents by rank({})", agentRank);
        return agentFacade.getAgentsByRank(agentRank);
    }

    @RequestMapping(value = "/codename/{codename}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final AgentDTO getAgentByCodeName(@PathVariable("codename") String codename) {
        logger.debug("rest get agent by codename({})", codename);
        return agentFacade.getAgentByCodeName(codename);
    }

    @RequestMapping(value = "/assign/{agentId}/{missionId}", method = RequestMethod.PUT)
    public final void assignAgentToMission(@PathVariable("agentId") Long agentId, @PathVariable("missionId") Long missionId) {
        logger.debug("rest assign agent {} to mission {}", agentId, missionId);
        this.agentFacade.assignAgentToMission(agentId, missionId);
    }

    @RequestMapping(value = "/remove/{agentId}/{missionId}", method = RequestMethod.PUT)
    public final void removeAgentFromMission(@PathVariable("agentId") Long agentId, @PathVariable("missionId") Long missionId) {
        logger.debug("rest remove agent {} to mission {}", agentId, missionId);
        this.agentFacade.removeAgentFromMission(agentId, missionId);
    }

    @RequestMapping(value = "/add/{agentId}/{departmentId}", method = RequestMethod.PUT)
    public final void addAgentToDepartment(@PathVariable("agentId") Long agentId, @PathVariable("departmentId") Long departmentId) {
        logger.debug("rest add agent {} to department {}", agentId, departmentId);
        this.agentFacade.addAgentToDepartment(agentId, departmentId);
    }

    @RequestMapping(value = "/language/{language}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<AgentDTO> getAgentsWithLanguageKnowledge(@PathVariable("language") LanguageEnum language) {
        logger.debug("rest get agents with language knowledge({})", language);
        return agentFacade.getAgentsWithLanguageKnowledge(language);
    }

    @RequestMapping(value = "/retiring", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<AgentDTO> getSoonRetiringAgents() {
        logger.debug("rest get soon retiring agents()");
        return agentFacade.getSoonRetiringAgents();
    }
}
