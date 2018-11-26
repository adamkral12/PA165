package cz.fi.muni.pa165.secretagency.dao;

import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of AgentDao interface.
 * @author Milos Silhar (433614)
 */
@Repository
public class AgentDaoImpl extends GenericDaoImpl<Agent> implements AgentDao {

    /**
     * Constructor
     * Sets this DaoImpl to work with CRUD operations from generic implementation.
     */
    public AgentDaoImpl() { super(Agent.class); }

    @Override
    public List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum language) {
        return em.createQuery("SELECT a FROM Agent a WHERE :language MEMBER OF a.languages", Agent.class)
                .setParameter("language", language).getResultList();
    }

    @Override
    public List<Agent> getAgentsWithRank(AgentRankEnum rank) {
        return em.createQuery("SELECT a FROM Agent a WHERE a.rank = :agentRank", Agent.class)
                .setParameter("agentRank", rank).getResultList();
    }

    @Override
    public Agent getAgentByCodename(String codename) {
        try {
            return em.createQuery("SELECT a FROM Agent a WHERE a.codeName = :codename", Agent.class)
                    .setParameter("codename", codename).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Agent> getSoonRetiringAgents() {
        LocalDate upperBirthDate = LocalDate.now().plusYears(1).minusYears(60);
        LocalDate lowerBirthDate = LocalDate.now().minusYears(60);
        return em.createQuery("SELECT a FROM Agent a WHERE a.birthDate BETWEEN :lowerDate AND :upperDate", Agent.class)
                .setParameter("upperDate", upperBirthDate).setParameter("lowerDate", lowerBirthDate)
                .getResultList();
    }

    @Override
    public Set<Agent> getAgentsWithCodeNames(Set<String> codeNames) {
        List<Agent> agents =  em.createQuery("SELECT a FROM Agent a WHERE a.codeName IN :codeNames", Agent.class)
                                .setParameter("codeNames", codeNames)
                                .getResultList();
        return new HashSet<>(agents);
    }
}
