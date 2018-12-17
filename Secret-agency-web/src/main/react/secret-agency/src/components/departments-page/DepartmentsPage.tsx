import * as React from "react";
import "../SharedStyles.css";
import {ChangeEvent, MouseEvent} from 'react';
import {IDepartment} from "../../types/Department";
import "../SharedStyles.css";

import {
    createDepartment, deleteDepartment,
    editDepartment,
    getAllDepartments,
    getSpecializations
} from "../../services/departmentService";
import {DepartmentShowRow} from "./DepartmentShowRow";
import {DepartmentEditRow} from "./DepartmentEditRow";
import * as Immutable from 'immutable';
import {defineAbility} from "../../config/ability";
import {SearchDepartmentsForm} from "./SearchDepartmentsForm";

interface IDepartmentsState {
    readonly departments: Immutable.Map<number, IDepartment>;
    readonly newDepartment: INewDepartment;
    readonly specializations: string[];
    readonly editedDepartmentId: number | null;
    readonly formErrors: string[];
}

interface INewDepartment {
    readonly city: string;
    readonly country: string;
    readonly latitude: string;
    readonly longitude: string;
    readonly specialization: string;
}

export class DepartmentsPage extends React.Component<{}, IDepartmentsState> {
    public async componentDidMount() {
        const departments = Immutable.Map<number, IDepartment>((await getAllDepartments()).map(
            (department: IDepartment) => [
                department.id, department
            ]
        ));
        const specializations = await getSpecializations();
        const newDepartment = {
            city: '',
            country: '',
            latitude: '',
            longitude: '',
            specialization: '',
        };
        this.setState({
            departments,
            newDepartment,
            specializations,
            editedDepartmentId: null,
            formErrors: [],
        });
    };

    private addDepartment = async (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        const {city, country, latitude, longitude, specialization} = this.state.newDepartment;
        const department: IDepartment = {
            id: 1, // temporary id, will be given from API
            city,
            country,
            latitude: parseFloat(latitude),
            longitude: parseFloat(longitude),
            specialization,
            agentIds: [],
        };
        const errors = this.validate(department);
        if (errors.length === 0) {
            const createdDepartment = await createDepartment(department);
            this.setState((prevState) => ({
                departments: prevState.departments.set(createdDepartment.id, createdDepartment)
            }));
            this.setState(_ => ({editedDepartmentId: null}));
        } else {
            this.setState( _ => ({formErrors: errors}));
        }

    };

    private validate = (department: IDepartment): string[] => {
        const {city, country,latitude, longitude, specialization} = department;
        const errors = [];
        if (!city.trim()) {
            errors.push("City must be set");
        }

        if (!country.trim()) {
            errors.push("Country must be set");
        }

        if (isNaN(latitude) || latitude > 90 || latitude < - 90) {
            errors.push("Latitude must be a number between -90 and 90");
        }

        if (isNaN(longitude) || longitude > 180 || latitude < -180) {
            errors.push("Longitude must be a number between -180 and 180");
        }

        if (!specialization.trim()) {
            errors.push("Specialization must be set");
        }
        return errors;
    };

    private onCityChange = (event: ChangeEvent<HTMLInputElement>): void => {
        const city = event.currentTarget.value;
        this.setState((prevState) => ( {newDepartment:  { ...prevState.newDepartment, city }}));
    };

    private onCountryChange = (event: ChangeEvent<HTMLInputElement>): void => {
        const country = event.currentTarget.value;
        this.setState((prevState) => ( {newDepartment:  { ...prevState.newDepartment, country }}));
    };

    private onLatitudeChange = (event: ChangeEvent<HTMLInputElement>): void => {
        const latitude = event.currentTarget.value;
        this.setState((prevState) => ( {newDepartment:  { ...prevState.newDepartment, latitude }}));
    };

    private onLongitudeChange = (event: ChangeEvent<HTMLInputElement>): void => {
        const longitude = event.currentTarget.value;
        this.setState((prevState) => ( {newDepartment:  { ...prevState.newDepartment, longitude }}));
    };

    private onSpecializationChange = (event: ChangeEvent<HTMLSelectElement>): void => {
        const specialization = event.currentTarget.value;
        this.setState((prevState) => ( {newDepartment:  { ...prevState.newDepartment, specialization }}));
    };

    private startEditDepartment = (editedDepartmentId: number): void => {
        this.setState(_ => ({
            editedDepartmentId,
        }))
    };

    private onCancelEditing = (): void => {
        this.setState(_ => ({editedDepartmentId: null}));
    };

    private onEdit = async (department: IDepartment) => {
        const errors = this.validate(department);
        if (errors.length === 0) {
            const editedDepartment = await editDepartment(department);
            this.setState((prevState) => ({
                departments: prevState.departments.set(editedDepartment.id, editedDepartment)
            }));
            this.setState(_ => ({editedDepartmentId: null}));
        } else {
            this.setState( _ => ({formErrors: errors}));
        }
    };

    private onSearchChange = (departments: Immutable.Map<number, IDepartment>) => {
        this.setState(_ => ({
            departments,
        }));
    };

    private onDelete = async (departmentId: number) => {
        const department = this.state.departments.get(departmentId);
        if (department.agentIds.length > 0) {
            this.setState(_ => ({
                formErrors: ["There are still some agents in this department, fire them before you tear down the building"]
            }))
        } else {
            try {
                await deleteDepartment(department.id);
                const departments = Immutable.Map<number, IDepartment>((await getAllDepartments()).map(
                    (dep: IDepartment) => [
                        dep.id, dep
                    ]
                ));

                this.setState(_ => ({
                    departments,
                    formErrors: [],
                }));

            } catch (e) {
                this.setState( _ => ({
                    formErrors: [e.message],
                }));
            }
        }
    };

    public render() {
        if (this.state) {
            const { departments } = this.state;
            const tableRows = departments.keySeq().map((key: number) =>
                this.state.editedDepartmentId === key ?
                    <DepartmentEditRow
                        key={key}
                        specializations={this.state.specializations}
                        department={departments.get(key)}
                        onCancelEdit={this.onCancelEditing}
                        onEdit={this.onEdit}
                    /> :
                <DepartmentShowRow
                    key={key}
                    department={departments.get(key)}
                    onStartEdit={() => this.startEditDepartment(key)}
                    onDelete={() => this.onDelete(key)}
                />
            );
            const {city, country,latitude, longitude, specialization} = this.state.newDepartment;
            return (
                <div>
                    <div className="table-wrapper">
                        {this.state.formErrors.length > 0 && <div className={'alert alert-danger'}>
                            {this.state.formErrors.map((error: string, index) =>
                                <p key={index}>{error}</p>
                            )}
                        </div>}
                        <table className="data-table">
                            <thead>
                            <tr>
                                <th>City</th>
                                <th>Country</th>
                                <th>Latitude</th>
                                <th>Longitude</th>
                                <th>Specialization</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            {tableRows}
                            {defineAbility().can('create', 'Department') &&
                            <tr>
                                <td>
                                    <input type={'text'} value={city}
                                           onChange={this.onCityChange}
                                    />
                                </td>
                                <td>
                                    <input type={'text'} value={country}
                                           onChange={this.onCountryChange}
                                    />
                                </td>
                                <td>
                                    <input type={'text'} value={latitude}
                                           onChange={this.onLatitudeChange}
                                    />
                                </td>
                                <td>
                                    <input type={'text'} value={longitude}
                                           onChange={this.onLongitudeChange}
                                    />
                                </td>
                                <td>
                                    <select value={specialization}
                                            onChange={this.onSpecializationChange}
                                    >
                                        <option value={""}/>
                                        {this.state.specializations.map((specializationOption: string) =>
                                            <option key={specializationOption}
                                                    value={specializationOption}>{specializationOption}</option>
                                        )}
                                    </select>
                                </td>
                                <td>
                                    <button
                                        className={"btn btn-primary"}
                                        type={'submit'}
                                        value={'create'}
                                        onClick={this.addDepartment}
                                    >
                                        Create
                                    </button>
                                </td>
                            </tr>
                            }
                            </tbody>
                        </table>
                    </div>
                    <SearchDepartmentsForm onSearch={this.onSearchChange}/>
                </div>
            )
        } else {
            return (
                <div>Loading table...</div>
            )
        }
    }
}
