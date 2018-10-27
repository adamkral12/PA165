package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;

import java.time.LocalDate;
import java.util.List;

public class AgentDaoImpl extends GenericDaoImpl<Agent> implements AgentDao {

    /**
     * Constructor
     * Sets this DaoImpl to work with CRUD operations from generic implementation.
     */
    public AgentDaoImpl() {super(Agent.class);}

    @Override
    public List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum language) {
        return em.createQuery("SELECT a FROM Agent a WHERE :lang MEMBER OF a.languages", Agent.class)
                .setParameter("lang", language).getResultList();
    }

    @Override
    public List<Agent> getAgentsWithRank(AgentRankEnum rank) {
        return em.createQuery("SELECT a FROM Agent a WHERE a.rank = :agentRank", Agent.class)
                .setParameter("agentRank", rank).getResultList();
    }

    @Override
    public Agent getAgentByCodename(String codename) {
        return em.createQuery("SELECT a FROM Agent a WHERE a.codeName = :code", Agent.class)
                .setParameter("code", codename).getSingleResult();
    }

    @Override
    public List<Agent> getSoonRetiringAgents() {
        LocalDate upperBirthDate = LocalDate.now().plusYears(1).minusYears(60);
        LocalDate lowerBirthDate = LocalDate.now().minusYears(60);
        return em.createQuery("SELECT a FROM Agent a WHERE a.birthDate <= :upperDate AND a.birthDate >= :lowerDate", Agent.class)
                .setParameter("upperDate", upperBirthDate).setParameter("lowerDate", lowerBirthDate)
                .getResultList();
    }
}
