import {GET, REST_URL_BASE} from "../utils/requestUtils";

export function getAllAgents(): Promise<any> {
    return GET(`${REST_URL_BASE}/agents`);
}