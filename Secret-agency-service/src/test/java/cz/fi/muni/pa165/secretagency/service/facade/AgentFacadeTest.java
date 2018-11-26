package cz.fi.muni.pa165.secretagency.service.facade;

import cz.fi.muni.pa165.secretagency.dto.AgentCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;
import cz.fi.muni.pa165.secretagency.facade.AgentFacade;
import cz.fi.muni.pa165.secretagency.service.AgentService;
import cz.fi.muni.pa165.secretagency.service.BeanMappingService;
import cz.fi.muni.pa165.secretagency.service.DepartmentService;
import cz.fi.muni.pa165.secretagency.service.MissionService;
import cz.fi.muni.pa165.secretagency.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Author: Adam Kral <433328>
 * Date: 11/25/18
 * Time: 3:57 PM
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AgentFacadeTest extends AbstractTestNGSpringContextTests {

    @Spy
    @Autowired
    private BeanMappingService beanMappingService;

    @Mock
    private AgentService agentService;

    @Mock
    private MissionService missionService;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private AgentFacade agentFacade = new AgentFacadeImpl();

    @BeforeMethod
    public void setUp() {
        setUpTestingData();
        MockitoAnnotations.initMocks(this);
    }

    private Agent jamesBond;
    private Agent abbeyChase;
    private Mission saveTheWorld;
    private Department fbi;

    private void setUpTestingData() {
        jamesBond = new Agent();
        jamesBond.setId(1L);
        jamesBond.setName("James Bond");
        jamesBond.setRank(AgentRankEnum.SENIOR);
        jamesBond.setCodeName("007");
        jamesBond.addLanguage(LanguageEnum.CZ);

        abbeyChase = new Agent();
        abbeyChase.setId(2L);
        abbeyChase.setRank(AgentRankEnum.JUNIOR);
        abbeyChase.addLanguage(LanguageEnum.CZ);

        saveTheWorld = new Mission();
        saveTheWorld.setId(5L);
        saveTheWorld.setMissionType(MissionTypeEnum.ESPIONAGE);

        fbi = new Department();
        fbi.setId(3L);
    }

    @Test
    public void getByIdTest() {
        when(agentService.getEntityById(jamesBond.getId())).thenReturn(jamesBond);
        AgentDTO agentDTO = agentFacade.getAgentById(jamesBond.getId());
        assertEquals(jamesBond, beanMappingService.mapTo(agentDTO, Agent.class));
    }

    @Test
    public void getAllTest() {
        when(agentService.getAll()).thenReturn(Arrays.asList(jamesBond,abbeyChase));
        List<AgentDTO> agentDTOS = agentFacade.getAllAgents();
        List<Agent> agents = beanMappingService.mapTo(agentDTOS, Agent.class);
        assertEquals(2,agents.size());
        assertTrue(agents.contains(jamesBond));
        assertTrue(agents.contains(abbeyChase));
    }

    @Test
    public void createTest() {
        AgentCreateDTO agentDTO = beanMappingService.mapTo(jamesBond, AgentCreateDTO.class);
        when(agentService.save(jamesBond)).thenReturn(jamesBond);
        assertEquals(jamesBond.getId(), agentFacade.createAgent(agentDTO));
    }

    @Test
    public void deleteTest() {
        agentFacade.deleteAgent(jamesBond.getId());
        verify(agentService).deleteEntityById(jamesBond.getId());
    }

    @Test
    public void getAgentsByRankTest() {
        when(agentService.getAgentsByRank(jamesBond.getRank())).thenReturn(Collections.singletonList(jamesBond));
        List<AgentDTO> agentDTOS = agentFacade.getAgentsByRank(jamesBond.getRank());
        List<Agent> agents = beanMappingService.mapTo(agentDTOS, Agent.class);
        assertEquals(1,agents.size());
        assertTrue(agents.contains(jamesBond));
    }

    @Test
    public void getAgentByCodename() {
        when(agentService.getAgentByCodeName(jamesBond.getCodeName())).thenReturn(jamesBond);
        AgentDTO agentDTO = agentFacade.getAgentByCodeName(jamesBond.getCodeName());
        assertEquals(jamesBond, beanMappingService.mapTo(agentDTO, Agent.class));
    }

    @Test
    public void getAgentsWithLanguageKnowledgeTest() {
        when(agentService.getAgentsWithLanguageKnowledge(LanguageEnum.CZ))
                .thenReturn(Arrays.asList(jamesBond, abbeyChase));
        List<AgentDTO> agentDTOS = agentFacade.getAgentsWithLanguageKnowledge(LanguageEnum.CZ);
        List<Agent> agents = beanMappingService.mapTo(agentDTOS, Agent.class);
        assertEquals(2,agents.size());
        assertTrue(agents.contains(jamesBond));
        assertTrue(agents.contains(abbeyChase));
    }

    @Test
    public void getSonRetiringAgentsTest() {
        when(agentService.getSoonRetiringAgents())
                .thenReturn(Arrays.asList(jamesBond, abbeyChase));
        List<AgentDTO> agentDTOS = agentFacade.getSoonRetiringAgents();
        List<Agent> agents = beanMappingService.mapTo(agentDTOS, Agent.class);
        assertEquals(2,agents.size());
        assertTrue(agents.contains(jamesBond));
        assertTrue(agents.contains(abbeyChase));
    }

    @Test
    public void assignAgentToMissionTest() {
        when(missionService.getEntityById(saveTheWorld.getId())).thenReturn(saveTheWorld);
        when(agentService.getEntityById(jamesBond.getId())).thenReturn(jamesBond);
        agentFacade.assignAgentToMission(jamesBond.getId(), saveTheWorld.getId());
        verify(agentService).assignAgentToMission(jamesBond, saveTheWorld);
    }

    @Test
    public void removeAgentFromMissionTest() {
        when(missionService.getEntityById(saveTheWorld.getId())).thenReturn(saveTheWorld);
        when(agentService.getEntityById(jamesBond.getId())).thenReturn(jamesBond);
        agentFacade.removeAgentFromMission(jamesBond.getId(), saveTheWorld.getId());
        verify(agentService).removeAgentFromMission(jamesBond, saveTheWorld);
    }

    @Test
    public void addAgentToDepartmentTest() {
        when(departmentService.getEntityById(fbi.getId())).thenReturn(fbi);
        when(agentService.getEntityById(jamesBond.getId())).thenReturn(jamesBond);
        agentFacade.addAgentToDepartment(jamesBond.getId(), fbi.getId());
        verify(agentService).addAgentToDepartment(jamesBond, fbi);
    }
}