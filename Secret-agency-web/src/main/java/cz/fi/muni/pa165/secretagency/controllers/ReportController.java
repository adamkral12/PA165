package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.dto.AgentDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportCreateDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportDTO;
import cz.fi.muni.pa165.secretagency.dto.ReportUpdateTextDTO;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.MissionResultReportEnum;
import cz.fi.muni.pa165.secretagency.enums.ReportStatus;
import cz.fi.muni.pa165.secretagency.facade.ReportFacade;
import cz.fi.muni.pa165.secretagency.service.exceptions.ReportServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.fi.muni.pa165.secretagency.security.AuthenticationSuccessHandler.AUTHENTICATED_USER_SESSION_KEY;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportFacade reportFacade;

    @RequestMapping(method = RequestMethod.GET)
    public List<ReportDTO> getAll() {
        return reportFacade.getAllReports();
    }

    @RequestMapping(value = "/report/{id}", method = RequestMethod.GET)
    public ReportDTO getReportById(@PathVariable Long id) {
        ReportDTO reportDTO = reportFacade.getReportById(id);
        if (reportDTO == null) {
            // TODO - throw exception
            throw new NullPointerException("Report with given id does not exist");
        }
        return reportDTO;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ReportDTO createReport(@Valid @RequestBody ReportCreateDTO reportCreateDTO) {
        // TODO - wrap with custom exception
        Long createdReportId = reportFacade.createReport(reportCreateDTO);
        return reportFacade.getReportById(createdReportId);
    }

    @RequestMapping(value = "/report/{id}", method = RequestMethod.DELETE)
    public void deleteReport(@PathVariable Long id) {
        // TODO - wrap with custom exception
        reportFacade.deleteReport(id);
    }

    @RequestMapping(value = "/report/{id}", method = RequestMethod.PUT)
    public ReportDTO updateReport(@PathVariable Long id, @Valid @RequestBody ReportUpdateTextDTO reportUpdateTextDTO) {
        // TODO - wrap with custom exception
        reportFacade.updateReportText(reportUpdateTextDTO);
        return reportFacade.getReportById(id);
    }

    @RequestMapping(value = "/report/{id}/approve", method = RequestMethod.PUT)
    public void approveReport(@PathVariable Long id, HttpServletRequest request) {
        AgentDTO authenticatedAgent = (AgentDTO) request.getSession().getAttribute(AUTHENTICATED_USER_SESSION_KEY);
        if (authenticatedAgent == null) {
            throw new NullPointerException("Unable to retrieve data about authenticated user");
        }
        if (authenticatedAgent.getRank() != AgentRankEnum.AGENT_IN_CHARGE) {
            // TODO throw custom exception
            throw new RuntimeException("You don't have permission to approve reports");
        }
        reportFacade.approveReport(id);
    }

    @RequestMapping(value = "/report/{id}/deny", method = RequestMethod.PUT)
    public void denyReport(@PathVariable Long id, HttpServletRequest request) {
        AgentDTO authenticatedAgent = (AgentDTO) request.getSession().getAttribute(AUTHENTICATED_USER_SESSION_KEY);
        if (authenticatedAgent == null) {
            throw new NullPointerException("Unable to retrieve data about authenticated user");
        }
        if (authenticatedAgent.getRank() != AgentRankEnum.AGENT_IN_CHARGE) {
            // TODO throw custom exception
            throw new RuntimeException("You don't have permission to approve reports");
        }
        reportFacade.approveReport(id);
    }

    @RequestMapping(value = "/interval/{dateFrom}/{dateTo}")
    public List<ReportDTO> getReportsFromInterval(@PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        return reportFacade.getReportsFromInterval(dateFrom, dateTo);
    }

    @RequestMapping(value = "/missonResult/{missionResult}")
    public List<ReportDTO> getReportsWithResult(@PathVariable MissionResultReportEnum missionResult) {
        return reportFacade.getReportsWithResult(missionResult);
    }

    @RequestMapping(value = "/reportStatus/{reportStatus}")
    public List<ReportDTO> getReportsWithStatus(@PathVariable ReportStatus reportStatus) {
        return reportFacade.getReportsWithStatus(reportStatus);
    }

    @RequestMapping(value = "/report/{reportId}/mentionedAgents")
    public Set<AgentDTO> getAgentsMentionedInReport(@PathVariable  Long reportId) {
        Set<AgentDTO> mentionedAgents = new HashSet<>();
        try {
            mentionedAgents = reportFacade.getAgentsMentionedInReport(reportId);
        } catch (ReportServiceException ex) {
            // TODO throw custom exception
        }
        return mentionedAgents;
    }

}