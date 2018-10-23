package cz.fi.muni.pa165.secretagency.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agent entity for the Secret agency project.
 *
 * @author (name = "Adam Kral", UCO = "<433328>")
 */

public class Agent {
    private Long id;

    private String name;

    private LocalDateTime birthDate;

    /**
     *
     * @param id of agent
     * @param name of agent
     * @param birthDate of agent
     */
    public Agent(Long id, String name, LocalDateTime birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
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
    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate agent's birth date
     */
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return Objects.equals(getName(), agent.getName()) &&
                Objects.equals(getBirthDate(), agent.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBirthDate());
    }
}
