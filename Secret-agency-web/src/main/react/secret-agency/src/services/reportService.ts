import {IReport, IReportCreate, IReportUpdate} from "../types/Report";
import {DELETE, GET, POST, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {AxiosError} from "axios";
import {IAgent} from "../types/Agent";


export function getAllReports(): Promise<IReport[]> {
    return GET<IReport[]>(`${REST_URL_BASE}/reports`).then((result) => {
       return result.data;
    });
}

/**
 * Creates new report. If report cannot be created, return error message.
 * @param newReport
 */
export function createNewReport(newReport: IReportCreate): Promise<IReport | string> {
    return POST<IReport>(`${REST_URL_BASE}/reports`, newReport).then((response => {
        return response.data;
    })).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

/**
 * Delete report with specific id. Returns true, if report was deleted. Otherwise returns false;
 * @param reportId id of the report
 */
export function deleteReport(reportId: number): Promise<boolean> {
    return DELETE<void>(`${REST_URL_BASE}/reports/report/${reportId}`).then(() => {
        return true;
    }).catch(() => {
        return false;
    });
}

/**
 * Get report with selected id. If report is not found, error message is returned.
 * @param reportId id of the report
 */
export function getReportById(reportId: number): Promise<IReport | string> {
    return GET<IReport>(`${REST_URL_BASE}/reports/report/${reportId}`).then((report) => {
        return report.data;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

/**
 * Update selected report. Returns updated report or error message.
 * @param reportUpdate data needed for updating report
 */
export async function updateReport(reportUpdate: IReportUpdate): Promise<IReport | string> {
    return PUT<IReport>(`${REST_URL_BASE}/reports/report/${reportUpdate.id}`, reportUpdate)
        .then(response => {
            return response.data
        }).catch((error: AxiosError) => {
            return error.response ? error.response.data.message : error.message;
        });
}

/**
 * Approve report with given id. Returns message error or nothing if report was approved.
 * @param reportId id of the report
 */
export function approveReport(reportId: number): Promise<void | string> {
    return PUT<void>(`${REST_URL_BASE}/reports/report/${reportId}/approve`).then(() => {
        return;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

/**
 * Deny report with given id. Returns message error or nothing if report was denied.
 * @param reportId id of the report
 */
export function denyReport(reportId: number): Promise<void | string> {
    return PUT<void>(`${REST_URL_BASE}/reports/report/${reportId}/deny`).then(() => {
        return;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

// /interval/{dateFrom}/{dateTo}
export function getReportsFromInterval(dateFrom: string, dateTo: string): Promise<IReport[] | string> {
    return GET<IReport[]>(`${REST_URL_BASE}/reports/interval/${dateFrom}/${dateTo}`).then((response) => {
        return response.data;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

// /missionResult/{missionResult}
export function getReportsWithResult(result: string): Promise<IReport[] | string> {
    return GET<IReport[]>(`${REST_URL_BASE}/reports/missionResult/${result}`).then((response) => {
        return response.data;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

// /reportStatus/{reportStatus}
export function getReportsWithStatus(status: string,): Promise<IReport[] | string> {
    return GET<IReport[]>(`${REST_URL_BASE}/reports/reportStatus/${status}`).then((response) => {
        return response.data;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}

// /report/{reportId}/mentionedAgents
export function getAgentsMentionedInReport(reportId: number): Promise<IAgent[] | string> {
    return GET<IReport[]>(`${REST_URL_BASE}/reports/report/${reportId}/mentionedAgents`).then((response) => {
        return response.data;
    }).catch((error: AxiosError) => {
        return error.response ? error.response.data.message : error.message;
    });
}