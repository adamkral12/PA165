package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.AgentDao;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.hibernate.service.spi.ServiceException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Author: Adam Kral <433328>
 * Date: 11/16/18
 * Time: 4:01 PM
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AgentServiceTest extends AbstractTestNGSpringContextTests {
    @Mock
    private AgentDao agentDao;

    @Autowired
    private AgentService agentService;

    @BeforeClass
    public void setup() throws ServiceException {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(agentService, "dao", agentDao);
    }

    /**
     * Test that all agents are returned
     */
    @Test
    public void getAllAgentsTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        when(agentDao.getAll()).thenReturn(Collections.singletonList(agent));
        assertEquals(1, agentService.getAll().size());
    }

    /**
     * Test that agent is created
     */
    @Test
    public void createAgentTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        agent.setCodeName("Rum");
        agent.setRank(AgentRankEnum.JUNIOR);
        when(agentDao.save(agent)).thenReturn(agent);
        Agent savedAgent = agentService.save(agent);
        assertEquals(agent, savedAgent);
    }

    /**
     * Test that correct agent with is returned by id
     */
    @Test
    public void getByIdTest() {
        Agent agent1 = new Agent();
        agent1.setId(50L);
        when(agentDao.getEntityById(agent1.getId())).thenReturn(agent1);
        assertEquals(agent1, agentService.getEntityById(agent1.getId()));
    }

    /**
     * Assert that agent with given rank are returned
     */
    @Test
    public void getAgentsWithRankTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        agent.setRank(AgentRankEnum.JUNIOR);
        when(agentDao.getAgentsWithRank(AgentRankEnum.JUNIOR)).thenReturn(Collections.singletonList(agent));
        List<Agent> agentList = agentService.getAgentsByRank(AgentRankEnum.JUNIOR);
        assertEquals(1, agentList.size());
        assertEquals(50L, (long) agentList.get(0).getId());
    }


    /**
     * Assert that agent with given codename is returned
     */
    @Test
    public void getAgentByCodenameTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        agent.setCodeName("Tequila");
        when(agentDao.getAgentByCodename("Tequila")).thenReturn(agent);
        Agent foundAgent = agentService.getAgentByCodeName("Tequila");
        assertEquals(50L, (long) foundAgent.getId());
    }

    /**
     * Assert that agent is assigned to given mission
     */
    @Test
    public void assignAgentToMissionTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        Mission mission = new Mission();
        mission.setId(1L);
        agentService.assignAgentToMission(agent, mission);
        assertEquals(1, agent.getMissions().size());
        assertEquals(1L, (long) agent.getMissions().get(0).getId());
    }

    /**
     * Assert that agent is removed from given mission
     */
    @Test
    public void removeAgentFromMissionTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        Mission mission = new Mission();
        mission.setId(1L);
        agent.addMission(mission);
        agentService.removeAgentFromMission(agent, mission);
        assertEquals(0, agent.getMissions().size());
    }

    /**
     * Assert that agent department is correctly changed
     */
    @Test
    public void addAgentToDepartmentTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        Department formerDepartment = new Department();
        formerDepartment.setId(1L);

        Department newDepartment = new Department();
        newDepartment.setId(2L);

        agent.setDepartment(formerDepartment);

        agentService.addAgentToDepartment(agent, newDepartment);
        assertEquals(2L, (long) agent.getDepartment().getId());
    }

    @Test
    public void getAgentsWithLanguageKnowledgeTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        agent.addLanguage(LanguageEnum.CZ);
        when(agentDao.getAgentsWithLanguageKnowledge(LanguageEnum.CZ)).thenReturn(Collections.singletonList(agent));

        List<Agent> agents = agentService.getAgentsWithLanguageKnowledge(LanguageEnum.CZ);
        assertEquals(1, agents.size());
        assertEquals(50L, (long) agents.get(0).getId());
    }

    @Test
    public void getSoonRetiringAgentsTest() {
        Agent agent = new Agent();
        agent.setId(50L);
        when(agentDao.getSoonRetiringAgents()).thenReturn(Collections.singletonList(agent));
        List<Agent> agents = agentService.getSoonRetiringAgents();
        assertEquals(1, agents.size());
        assertEquals(50L, (long) agents.get(0).getId());
    }

    @Test
    public void getAgentsWithCodeNamesTest() {
        Agent pepa = new Agent();
        pepa.setId(3L);
        pepa.setCodeName("Pepa");
        Set<Agent> agentsWithCodenames = new HashSet<>();
        agentsWithCodenames.add(pepa);

        Set<String> codeNames = new HashSet<>();
        codeNames.add("Pepa");
        when(agentDao.getAgentsWithCodeNames(codeNames)).thenReturn(agentsWithCodenames);
        Set<Agent> agents = agentService.getAgentsWithCodeNames(codeNames);
        assertEquals(agents.size(), 1);
        assertTrue(agents.stream().anyMatch(agent -> agent.getId().equals(3L)));
    }
}