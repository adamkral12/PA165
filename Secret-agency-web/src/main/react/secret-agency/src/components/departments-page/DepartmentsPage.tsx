import * as React from "react";
import "../SharedStyles.css";
import {IDepartment} from "../../types/Department";
import {getAllDepartments} from "../../services/departmentService";

interface IDepartmentsState {
    departments: IDepartment[]
}

export class DepartmentsPage extends React.Component<any, IDepartmentsState> {
    constructor(props: any) {
        super(props);

        getAllDepartments().then(
            response => {
                console.log(response);
                const departments = response.data as IDepartment[];
                this.setState({departments});
            }
        )
    }

    public render() {
        if (this.state) {
            const tableRows = this.state.departments.map(department =>
                <tr key={department.id}>
                    <td>{department.city}</td>
                    <td>{department.country}</td>
                    <td>{department.latitude}</td>
                    <td>{department.longitude}</td>
                </tr>
            );
            return (
                <div className="table-wrapper">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>City</th>
                            <th>Country</th>
                            <th>Latitude</th>
                            <th>Longitude</th>
                        </tr>
                        </thead>
                        <tbody>
                        {tableRows}
                        </tbody>
                    </table>
                </div>
            )
        } else {
            return (
                <div>Loading table...</div>
            )
        }
    }
}
