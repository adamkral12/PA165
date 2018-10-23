package cz.fi.muni.pa165.secretagency.entity;

import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;

import java.time.LocalDate;

/**
 * Report entity for Secret agency project.
 *
 * @author Jan Pavlu (487548)
 */
public class Report {

    private Long id;
    private String text;
    private LocalDate date;
    private MissionResultReportEnum missionResult;

    /**
     * Constructor
     * @param text text of the report
     * @param date date when report was created
     * @param missionResult result of agent's work during the mission
     */
    public Report(String text, LocalDate date, MissionResultReportEnum missionResult) {
        this.text = text;
        this.date = date;
        this.missionResult = missionResult;
    }

    /**
     * Empty constructor
     */
    public Report() {
    }

    // GETTERS AND SETTERS
    /**
     * @return id of the report
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id id of the report
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return text of the report
     */
    public String getText() {
        return text;
    }

    /**
     * @param text text of the report
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return date when report was created
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date date when report was created
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return result of agent's work during the mission
     */
    public MissionResultReportEnum getMissionResult() {
        return missionResult;
    }

    /**
     * @param missionResult result of agent's work during the mission
     */
    public void setMissionResult(MissionResultReportEnum missionResult) {
        this.missionResult = missionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;

        Report report = (Report) o;

        if (!getText().equals(report.getText())) return false;
        if (!getDate().equals(report.getDate())) return false;
        return getMissionResult() == report.getMissionResult();
    }

    @Override
    public int hashCode() {
        int result = getText().hashCode();
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getMissionResult().hashCode();
        return result;
    }
}
