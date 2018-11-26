package cz.fi.muni.pa165.secretagency.dto;

import cz.fi.muni.pa165.secretagency.enums.MissionTypeEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for creating new Mission.
 * @author Adam Skurla (487588)
 */
public class MissionCreateDTO {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private MissionTypeEnum missionType;

    @NotNull
    private LocalDate started;

    private LocalDate ended;

    private Set<Long> agentIds;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public MissionTypeEnum getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionTypeEnum missionType) {
        this.missionType = missionType;
    }

    public LocalDate getStarted() {
        return started;
    }

    public void setStarted(LocalDate started) {
        this.started = started;
    }

    public LocalDate getEnded() {
        return ended;
    }

    public void setEnded(LocalDate ended) {
        this.ended = ended;
    }

    public Set<Long> getAgentIds() {
        return agentIds;
    }

    public void setAgentIds(Set<Long> agentIds) {
        this.agentIds = agentIds;
    }

    @Override
    public String toString() {
        return "ReportCreateDTO{" +
                "latitude='" + latitude +
                ", longitude=" + longitude +
                ", missionType=" + missionType +
                ", started=" + started +
                ", ended=" + ended +
                ", agentIds=" + agentIds +
                '}';
    }
}
