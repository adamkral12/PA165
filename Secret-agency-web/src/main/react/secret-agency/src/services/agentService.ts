import {GET, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IAgent} from "../types/Agent";
import {INewAgent} from "../components/agents-page/AgentsPage";

export function getAllAgents(): Promise<IAgent[]> {
    return GET(`${REST_URL_BASE}/agents`).then(
        response => {
            return response.data as IAgent[];
        }
    );
}

export function getAgentRanks(): Promise<string[]> {
    return GET(`${REST_URL_BASE}/agents/ranks`).then(
        response => {
            return response.data as string[];
        }
    );
}

export function getAllLanguages(): Promise<string[]> {
    return GET(`${REST_URL_BASE}/agents/languages`).then(
        response => {
            return response.data as string[];
        }
    );
}

export const editAgent = (data: INewAgent): Promise<IAgent> => (
    PUT(`${REST_URL_BASE}/agents/${data.id}`, data).then(
        response => {
            return response.data as IAgent;
        }
    )
);