import {GET, REST_URL_BASE} from "../utils/requestUtils";

export function getAllMissions(): Promise<any> {
    return GET(`${REST_URL_BASE}/missions`);
}