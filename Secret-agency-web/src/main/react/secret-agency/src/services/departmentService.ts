import {GET, POST, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IDepartment} from "../types/Department";

export function getAllDepartments(): Promise<IDepartment[]> {
    return GET(`${REST_URL_BASE}/departments`).then(
        response => {
            return response.data as IDepartment[];
        }
    )
}

export const createDepartment = (data: IDepartment): Promise<IDepartment> => (
    POST(`${REST_URL_BASE}/departments`, data).then(
        response => {
            return response.data as IDepartment;
        }
    )
);

export const getSpecializations = (): Promise<string[]> => (
    GET(`${REST_URL_BASE}/departments/specializations`).then(
        response => {
            return response.data as string[];
        }
    )
);

export const editDepartment = (data: IDepartment): Promise<IDepartment> => (
    PUT(`${REST_URL_BASE}/departments/${data.id}`, data).then(
        response => {
            return response.data as IDepartment;
        }
    )
);