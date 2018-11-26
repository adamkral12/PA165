package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.ReportDao;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.entity.Report;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import cz.fi.muni.pa165.secretagency.service.exceptions.ReportServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ReportService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * @author Jan Pavlu
 */
@Service
public class ReportServiceImpl extends GenericServiceImpl<Report, ReportDao> implements ReportService {

    @Autowired
    private AgentService agentService;

    @Override
    public List<Report> getReportsFromInterval(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new NullPointerException("Date from and date to must be set.");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new ReportServiceException("Date from cannot have higher value than date to");
        }
        return getDao().getReportsFromInterval(dateFrom, dateTo);
    }

    @Override
    public List<Report> getReportsWithResult(MissionResultReportEnum missionResultReport) {
        if (missionResultReport == null) {
            throw new NullPointerException("Mission result must be set");
        }
        return getDao().getReportsWithResult(missionResultReport);
    }

    @Override
    public List<Report> getReportsWithStatus(ReportStatus reportStatus) {
        if (reportStatus == null) {
            throw new NullPointerException("Report status must be set");
        }
        return getDao().getReportsWithStatus(reportStatus);
    }

    @Override
    public List<Report> getReportsWithStatusFromMission(ReportStatus reportStatus, Mission mission) {
        if (reportStatus == null) {
            throw new NullPointerException("Report status must be set");
        }

        if (mission == null) {
            throw new NullPointerException("Mission must be set");
        }
        return getDao().getReportsWithStatusFromMission(reportStatus, mission);
    }

    @Override
    public void updateReportText(Report report, String text) {
        if (report == null) {
            throw new NullPointerException("Report cannot be updated, because it wasn't found");
        }

        if (text == null) {
            throw new NullPointerException("Text must be set");
        }

        report.setText(text);
        report.setReportStatus(ReportStatus.UPDATED);
    }

    @Override
    public void changeReportStatus(Report report, ReportStatus reportStatus) {
        if (report == null) {
            throw new NullPointerException("Report status cannot be updated, because  report wasn't found");
        }

        if (reportStatus == null) {
            throw new NullPointerException("Report status must be set");
        }

        report.setReportStatus(reportStatus);
    }


    @Override
    public Set<Agent> getAgentsMentionedInReport(Long reportId) {
        if (reportId == null) {
            throw new NullPointerException("Report id must be set");
        }

        Report report = getEntityById(reportId);
        if (report == null) {
            throw new ReportServiceException("Report with given id does not exist");
        }

        String reportText = report.getText().toLowerCase();
        List<String> allAgentsCodeNames = agentService.getAll().stream().map(Agent::getCodeName)
                                                                        .collect(Collectors.toList());
        if (allAgentsCodeNames.isEmpty()) {
            throw new ReportServiceException("Database does not contain any agents");
        }

        String agentCodeNamesPatternString = createPatternFromAgentCodeNames(allAgentsCodeNames);
        Pattern pattern = Pattern.compile("(?i)" + agentCodeNamesPatternString);

        Matcher matcher = pattern.matcher(reportText);
        Set<String> matchedCodeNames = getMatchedCodenames(matcher);

        Set<Agent> agentsInReport = agentService.getAgentsWithCodeNames(matchedCodeNames);
        return agentsInReport;
    }

    /**
     * Creates pattern for regex from list of codenames. StringBuilder is used because it's more
     *   efficient than regular String interpolation. Agent codename must be prefixed with word agent.
     *
     * The reason is described in:
     * {@link cz.fi.muni.pa165.secretagency.service.ReportService#getAgentsMentionedInReport(Long)}
     *
     * For example: [bures, black, bond] => "agent (bures)|agent (black)|agent (bond)"
     *
     * @param codeNames codenames of existing agents
     * @return pattern for regexp
     */
    private String createPatternFromAgentCodeNames(List<String> codeNames) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String codeName : codeNames) {
            boolean isFirstCodename = codeName.equals(codeNames.get(0));
            if (isFirstCodename) {
                // open non-capturing group
                stringBuilder.append("(?:");
            }

            // add codename
            stringBuilder.append("agent (").append(codeName).append(")");

            boolean isLastCodename = codeName.equals(codeNames.get(codeNames.size() - 1));
            if (!isLastCodename) {
                // prepare for next codename
                stringBuilder.append("|");
            } else {
                // close non-capturing group
                stringBuilder.append(")");
            }
        }
        return stringBuilder.toString().toLowerCase();
    }

    /**
     * Returns codenames which appeared in the text.
     *
     * @param matcher matcher object
     * @return codenames which appeared in the text
     */
    private Set<String> getMatchedCodenames(Matcher matcher) {
        Set<String> matchedCodeNames = new HashSet<>();
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String match = matcher.group(i);
                if (match == null) {
                    continue;
                }
                // save only agent codename - without thw word "agent"
                if (match.toLowerCase().contains("agent ")) {
                    match = match.replace("agent ", "");
                }
                matchedCodeNames.add(match);
            }
        }
        return matchedCodeNames;
    }
}
