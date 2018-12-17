package cz.fi.muni.pa165.secretagency.dto;

import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adam Skurla (487588)
 * Agent update DTO
 */
public class AgentUpdateDTO {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @DateTimeFormat
    private LocalDate birthDate;

    @NotNull
    @NotEmpty
    private Set<LanguageEnum> languages = new HashSet<>();

    @NotNull
    private AgentRankEnum rank;

    @NotNull
    private String codeName;

    @NotNull
    private Long departmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<LanguageEnum> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<LanguageEnum> languages) {
        this.languages = languages;
    }

    public AgentRankEnum getRank() {
        return rank;
    }

    public void setRank(AgentRankEnum rank) {
        this.rank = rank;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "AgentUpdateDTO{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", languages=" + languages +
                ", rank=" + rank +
                ", codeName='" + codeName + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
