import {GET, POST, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IDepartment} from "../types/Department";

export const getAllDepartments = (): Promise<IDepartment[]> => {
    return GET<IDepartment[]>(`${REST_URL_BASE}/departments`).then(
        response => {
            return response.data;
        }
    )
};

export const createDepartment = (data: IDepartment): Promise<IDepartment> => (
    POST<IDepartment>(`${REST_URL_BASE}/departments`, data).then(
        response => {
            return response.data;
        }
    )
);

export const getSpecializations = (): Promise<string[]> => (
    GET<string[]>(`${REST_URL_BASE}/departments/specializations`).then(
        response => {
            return response.data;
        }
    )
);

export const editDepartment = (data: IDepartment): Promise<IDepartment> => (
    PUT<IDepartment>(`${REST_URL_BASE}/departments/${data.id}`, data).then(
        response => {
            return response.data;
        }
    )
);