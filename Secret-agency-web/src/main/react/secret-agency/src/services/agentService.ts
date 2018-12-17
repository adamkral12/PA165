import {GET, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IAgent} from "../types/Agent";
import {INewAgent} from "../components/agents-page/AgentsPage";

export function getAllAgents(): Promise<IAgent[]> {
    return GET<IAgent[]>(`${REST_URL_BASE}/agents`).then(
        response => {
            return response.data;
        }
    );
}

export function getAgentRanks(): Promise<string[]> {
    return GET<string[]>(`${REST_URL_BASE}/agents/ranks`).then(
        response => {
            return response.data;
        }
    );
}

export function getAllLanguages(): Promise<string[]> {
    return GET<string[]>(`${REST_URL_BASE}/agents/languages`).then(
        response => {
            return response.data;
        }
    );
}

export const editAgent = (data: INewAgent): Promise<IAgent> => (
    PUT<IAgent>(`${REST_URL_BASE}/agents/${data.id}`, data).then(
        response => {
            return response.data;
        }
    )
);