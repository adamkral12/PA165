package cz.fi.muni.pa165.secretagency.enums;

/**
 * Corresponds to possible results of the mission. Every report should
 * have result in which an agent reports if he was successful.
 *
 * @author Jan Pavlu (487548)
 */
public enum MissionResultReportEnum {
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private String result;

    MissionResultReportEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
