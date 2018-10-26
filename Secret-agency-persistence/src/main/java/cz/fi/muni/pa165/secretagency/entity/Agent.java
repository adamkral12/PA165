package cz.fi.muni.pa165.secretagency.entity;

import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

/**
 * Agent entity for the Secret agency project.
 *
 * @author (Adam Kral<433328>)
 */
@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;

    @ElementCollection
    @NotNull
    private Set<LanguageEnum> languages = new HashSet<>();

    @Enumerated
    @NotNull
    private AgentRankEnum rank;

    @NotNull
    private String codeName;

    @ManyToMany
    @JoinTable(name = "agent_mission",
            joinColumns = @JoinColumn(name = "agent_id"),
            inverseJoinColumns = @JoinColumn(name = "mission_id")
    )
    private List<Mission> missions = new ArrayList<Mission>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull
    private Department department;
    /**
     *
     * @param id of agent
     * @param name of agent
     * @param birthDate of agent
     * @param languages agent can speak these languages
     * @param rank of agent
     */
    public Agent(Long id, String name, LocalDate birthDate, Set<LanguageEnum> languages, AgentRankEnum rank, String codeName, List<Mission> missions, Department department) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.languages = languages;
        this.rank = rank;
        this.codeName = codeName;
        this.missions = missions;
        this.department = department;
    }

    /**
     * @param id of agent
     */
    public Agent(Long id) {
        this.id = id;
    }

    /** Create empty Agent **/
    public Agent() {}

    /**
     * @return Agent's id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id agent's id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return agent'a name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name agent's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return agent's birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @return agents' known languages
     */
    public Set<LanguageEnum> getLanguages() {
        return languages;
    }

    /**
     * @param languages agent's newly learned languages
     */
    public void setLanguages(Set<LanguageEnum> languages) {
        this.languages = languages;
    }

    /**
     * @return agents' rank
     */
    public AgentRankEnum getRank() {
        return rank;
    }

    /**
     * @param rank agents rank
     */
    public void setRank(AgentRankEnum rank) {
        this.rank = rank;
    }

    /**
     * @param birthDate agent's birth date
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return agent's code name
     */
    public String getCodeName() {
        return codeName;
    }

    /**
     * @param codeName set agent's code name
     */
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    /**
     * @return List of agent's missions
     */
    public List<Mission> getMissions() {
        return missions;
    }

    /**
     * @param mission adds mission to agent's mission
     */
    public void addMission(Mission mission) {
        this.missions.add(mission);
    }

    /**
     * @return get department in which agent is working
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param department set department in which agent is working
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return Objects.equals(getName(), agent.getName()) &&
                Objects.equals(getBirthDate(), agent.getBirthDate()) &&
                Objects.equals(getLanguages(), agent.getLanguages()) &&
                Objects.equals(getRank(), agent.getRank()) &&
                Objects.equals(getCodeName(), agent.getCodeName()) &&
                Objects.equals(getMissions(), agent.getMissions()) &&
                Objects.equals(getDepartment(), agent.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBirthDate(), getLanguages(), getRank(), getCodeName(), getMissions(), getDepartment());
    }
}
