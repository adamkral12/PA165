import axios, {AxiosPromise} from "axios";

// absolute path is used to be able send requests both from node server and tomcat
export const URL_BASE = "http://localhost:8080/pa165";
export const REST_URL_BASE = "http://localhost:8080/pa165/rest";

// helper request methods - for authorized request
export function GET<T>(url: string): AxiosPromise<T> {
  return axios.get<T>(url, {withCredentials: true});
}

export function POST<T>(url: string, data?: any): AxiosPromise<T> {
  return axios.post<T>(url, data, {withCredentials: true});
}

export function PUT<T>(url: string, data: any): AxiosPromise<T> {
  return axios.put<T>(url, data, {withCredentials: true});
}

export function DELETE<T>(url: string): AxiosPromise<T> {
  return axios.delete(url, {withCredentials: true});
}
