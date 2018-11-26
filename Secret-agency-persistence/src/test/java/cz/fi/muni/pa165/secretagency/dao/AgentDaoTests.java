package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.SecretAgencyPersistenceApplicationContext;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Adam Skurla (487588)
 */
@ContextConfiguration(classes = SecretAgencyPersistenceApplicationContext.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class AgentDaoTests extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private AgentDao agentDao;

    private Agent agent1;
    private Agent agent2;

    @BeforeClass
    public void createAgents() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Department department = new Department();
        department.setCity("Dolne oresany");
        department.setCountry("SK");
        department.setLatitude(2d);
        department.setLongitude(1d);
        department.setSpecialization(DepartmentSpecialization.INTELLIGENCE);

        this.agent1 = new Agent();
        agent1.setName("Janko Hrasko");
        agent1.setBirthDate(LocalDate.of(1980, 2, 2));
        HashSet<LanguageEnum> languageEnums = new HashSet<>(Arrays.asList(LanguageEnum.EN, LanguageEnum.RU));
        agent1.setLanguages(languageEnums);
        agent1.setRank(AgentRankEnum.SENIOR);
        agent1.setCodeName("Agent 1");
        agent1.setDepartment(department);

        this.agent2 = new Agent();
        agent2.setName("Vcielka Maja");
        agent2.setBirthDate(LocalDate.of(1959, 2, 2));
        HashSet<LanguageEnum> languageEnums2 = new HashSet<>(Arrays.asList(LanguageEnum.CZ, LanguageEnum.NO));
        agent2.setLanguages(languageEnums2);
        agent2.setRank(AgentRankEnum.AGENT_IN_CHARGE);
        agent2.setCodeName("Agent 0");
        agent2.setDepartment(department);

        department.addAgent(agent1);
        department.addAgent(agent2);

        entityManager.persist(department);
        entityManager.persist(agent1);
        entityManager.persist(agent2);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void getById() {
        Agent testedAgent = agentDao.getEntityById(this.agent1.getId());
        assertEquals(testedAgent.getName(), "Janko Hrasko");
        assertEquals(testedAgent.getBirthDate(), LocalDate.of(1980, 2, 2));
        assertEquals(testedAgent.getLanguages(), new HashSet<>(Arrays.asList(LanguageEnum.EN, LanguageEnum.RU)));
        assertEquals(testedAgent.getRank(), AgentRankEnum.SENIOR);
        assertEquals(testedAgent.getCodeName(), "Agent 1");
    }

    @Test
    public void getByInvalidId() {
        assertNull(agentDao.getEntityById(4321L));
    }

    @Test
    public void getAgentsWithLanguageKnowledge() {
        List<Agent> agents = agentDao.getAgentsWithLanguageKnowledge(LanguageEnum.EN);
        assertEquals(agents.size(), 1);
    }

    @Test
    public void getAgentsWithLanguageKnowledgeNotFound() {
        List<Agent> agents = agentDao.getAgentsWithLanguageKnowledge(LanguageEnum.JP);
        assertEquals(agents.size(), 0);
    }

    @Test
    public void getAgentsWithRank() {
        List<Agent> agents = agentDao.getAgentsWithRank(AgentRankEnum.AGENT_IN_CHARGE);
        assertEquals(agents.size(), 1);
    }

    @Test
    public void getAgentsWithRankNotFound() {
        List<Agent> agents = agentDao.getAgentsWithRank(AgentRankEnum.TRAINEE);
        assertEquals(agents.size(), 0);
    }

    @Test
    public void getAgentByCodename() {
        Agent agent = agentDao.getAgentByCodename("Agent 0");
        assertEquals(agent.getId(), this.agent2.getId());
    }

    @Test
    public void getAgentByCodenameNotFound() {
        Agent agent = agentDao.getAgentByCodename("codename");
        assertNull(agent);
    }

    @Test
    public void getSoonRetiringAgents() {
        List<Agent> agents = agentDao.getSoonRetiringAgents();
        assertEquals(agents.size(), 1);
    }

    @Test
    public void getAgentsWithCodeNames() {
        Set<String> codeNames = new HashSet<>();
        codeNames.add(agent1.getCodeName());
        codeNames.add(agent2.getCodeName());
        Set<Agent> agents = agentDao.getAgentsWithCodeNames(codeNames);
        assertEquals(agents.size(), 2);
        assertTrue(agents.contains(agent1));
        assertTrue(agents.contains(agent2));
    }

    @Test
    public void getAgentsWithCodeNamesNoAgents() {
        Set<String> codeNames = new HashSet<>();
        codeNames.add("Bob");
        codeNames.add("Bobek");
        assertEquals(agentDao.getAgentsWithCodeNames(codeNames).size(), 0);
    }
}
