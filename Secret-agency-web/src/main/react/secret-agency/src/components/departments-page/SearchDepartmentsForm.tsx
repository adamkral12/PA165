import * as React from "react";
import {ChangeEvent, MouseEvent} from "react";
import {IDepartment} from "../../types/Department";
import {
    getAllDepartments,
    getDepartmentsByCity,
    getDepartmentsByCountry,
    getDepartmentsBySpecialization
} from "../../services/departmentService";
import * as Immutable from "immutable";

interface ISearchDepartmentsFormState {
    readonly searchValue: string;
    readonly searchType: string;
    readonly searchTypes: string[];
    readonly serverError: string;
}

interface ISearchDepartmentsFormProps {
    readonly onSearch: (departments: Immutable.Map<number, IDepartment>) => void;
}

const SEARCH_BY_VALUE_CITY = 'city';
const SEARCH_BY_VALUE_COUNTRY = 'country';
const SEARCH_BY_VALUE_SPECIALIZATION = 'specialization';
const SEARCH_BY_VALUE_NOTHING = '';

const searchByValues = [
    SEARCH_BY_VALUE_CITY,
    SEARCH_BY_VALUE_COUNTRY,
    SEARCH_BY_VALUE_SPECIALIZATION,
    SEARCH_BY_VALUE_NOTHING,
];

export class SearchDepartmentsForm extends React.Component<ISearchDepartmentsFormProps, ISearchDepartmentsFormState> {
    constructor(props: ISearchDepartmentsFormProps) {
        super(props);
        this.state = {
            searchType: "",
            searchValue: "",
            searchTypes: searchByValues,
            serverError: "",
        }
    }

    private onSearchValueChange = (event: ChangeEvent<HTMLInputElement>): void => {
        event.preventDefault();
        const searchValue = event.currentTarget.value;
        this.setState(_ => ({
            searchValue,
        }))
    };

    private onSearchTypeChange = (event: ChangeEvent<HTMLSelectElement>): void => {
        event.preventDefault();
        const searchType = event.currentTarget.value;
        this.setState(_ => ({
            searchType,
        }))
    };

    private onSearch = async (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        let departments = [];
        if (!this.state.searchValue.trim()) {
            this.setState( _ => ({
                serverError: "Search parameter cannot be empty",
            }));
            return;
        }
        try {
            switch (this.state.searchType) {
                case SEARCH_BY_VALUE_CITY:
                    departments = await getDepartmentsByCity(this.state.searchValue);
                    break;
                case SEARCH_BY_VALUE_COUNTRY:
                    departments = await getDepartmentsByCountry(this.state.searchValue);
                    break;
                case SEARCH_BY_VALUE_SPECIALIZATION:
                    departments = await getDepartmentsBySpecialization(this.state.searchValue);
                    break;
                default:
                    departments = await getAllDepartments();
                    break;
            }

            this.props.onSearch(
                Immutable.Map<number, IDepartment>((departments).map(
                    (department: IDepartment) => [
                        department.id, department
                    ]
                )));

            this.setState(_ => ({
                serverError: "",
            }));
        } catch (e) {
            this.setState(_ => ({serverError: e.message}));
        }
    };

    public render() {
        return (
            <div className={"search-by-form row"}>
                {this.state.serverError && <div className={"alert alert-danger col-sm-12"}>{this.state.serverError}</div>}
                <div className="input-group mb-3 search-by-form-input">
                    <div className="input-group-prepend">
                        <span className="input-group-text" id="basic-addon1">Filter departments:</span>
                    </div>
                    <input
                        className="form-control"
                        value={this.state.searchValue}
                        onChange={this.onSearchValueChange}
                    />
                </div>
                <div className="input-group mb-3 search-by-form-input">
                    <div className="input-group-prepend">
                        <span className="input-group-text" id="basic-addon1">Filter by</span>
                    </div>
                    <select value={this.state.searchType}
                            onChange={this.onSearchTypeChange}
                            className={"form-control"}
                    >
                        {this.state.searchTypes.map((searchType: string) =>
                            <option key={searchType} value={searchType}>{searchType}</option>
                        )}
                    </select>

                    <div className="input-group-append">
                        <button className={'btn btn-outline-secondary btn-success'}
                                onClick={this.onSearch}
                        >Search
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}
