import {GET, POST, PUT, REST_URL_BASE} from "../utils/requestUtils";
import {IMission} from "../types/Mission";

export function getAllMissions(): Promise<IMission[]> {
    return GET<IMission[]>(`${REST_URL_BASE}/missions`).then(response => {
        return response.data as IMission[];
    });
}

export function getAllTypes(): Promise<string[]> {
    return GET<string[]>(`${REST_URL_BASE}/missions/types`).then(response => {
        return response.data as string[];
    });
}

export const getMission = (missionId: number): Promise<IMission> => {
    return GET<IMission>(`${REST_URL_BASE}/missions/${missionId}`).then(response =>{
       return response.data as IMission;
    });
};

export const createMission = (data: IMission): Promise<IMission> => (
    POST<IMission>(`${REST_URL_BASE}/missions`, data).then(
        response => {
            return response.data as IMission;
        }
    )
);

export const updateMission = (data: IMission): Promise<IMission> => (
    PUT<IMission>(`${REST_URL_BASE}/missions/${data.id}`, data).then(
        response => {
            return response.data as IMission;
        }
    )
);
