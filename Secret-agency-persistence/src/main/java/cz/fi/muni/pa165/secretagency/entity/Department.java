package cz.fi.muni.pa165.secretagency.entity;


import cz.fi.muni.pa165.secretagency.enums.DepartmentSpecialization;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Department entity for Secret Agency.
 *
 * @author Milos Silhar (433614)
 */
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    @NotNull
    private String country;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @Enumerated
    private DepartmentSpecialization specialization;

    @OneToMany
    private List<Agent> agents = new ArrayList<>();

    public Department() {
    }

    /**
     * @return Id of department
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id of department
     * @param id Department's id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return City of department
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city of department
     * @param city Department's city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Country of department
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country for department
     * @param country Department's country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return Latitude coordinate of department's location
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude coordinate of department
     * @param latitude Department's latitude coordinate
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return Longitude coordinate of department's location
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude coordinate of department
     * @param longitude Department's longitude coordinate
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return Department's specialization
     */
    public DepartmentSpecialization getSpecialization() {
        return specialization;
    }

    /**
     * Sets specialization of department.
     * @param specialization Department's specialization
     */
    public void setSpecialization(DepartmentSpecialization specialization) {
        this.specialization = specialization;
    }

    /**
     * @return All agents assotiated with this department.
     */
    public List<Agent> getAgents() {
        return Collections.unmodifiableList(this.agents);
    }

    /**
     * Adds agent to this department.
     * @param agent Agent to be added to this department.
     */
    public void addAgent(Agent agent) {
        this.agents.add(agent);
        agent.setDepartment(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude()) &&
                Objects.equals(getSpecialization(), that.getSpecialization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getCountry(), getLatitude(), getLongitude(), getSpecialization());
    }
}
