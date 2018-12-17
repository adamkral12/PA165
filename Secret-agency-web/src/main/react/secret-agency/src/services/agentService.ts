import {GET, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IAgent} from "../types/Agent";
import {INewAgent} from "../components/agents-page/AgentsPage";

export function getAllAgents(): Promise<any> {
    return GET(`${REST_URL_BASE}/agents`);
}

export function getAgentRanks(): Promise<any> {
    return GET(`${REST_URL_BASE}/agents/ranks`);
}

export function getAllLanguages(): Promise<any> {
    return GET(`${REST_URL_BASE}/agents/languages`);
}

export const editAgent = (data: INewAgent): Promise<IAgent> => (
    PUT(`${REST_URL_BASE}/agents/${data.id}`, data).then(
        response => {
            return response.data as IAgent;
        }
    )
);