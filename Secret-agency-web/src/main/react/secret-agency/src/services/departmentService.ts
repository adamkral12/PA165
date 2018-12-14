import {GET, REST_URL_BASE} from "../utils/requestUtils";

export function getAllDepartments(): Promise<any> {
    return GET(`${REST_URL_BASE}/departments`);
}