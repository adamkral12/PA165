import * as React from "react";
import {ChangeEvent, FormEvent} from "react";
import {Alert, Button} from "react-bootstrap";
import * as missionService from "../../services/missionService";
import {Redirect} from "react-router";
import {ROUTING_URL_BASE} from "../../utils/requestUtils";
import {Link} from "react-router-dom";
import {IMission} from "../../types/Mission";

// INTERFACES FOR COMPONENT
interface IMissionEditPageProps {
    missionId: number;
    isAuthenticatedUserAdmin: boolean;
}

interface IMissionEditPageState {
    // form data
    name: string;
    latitude: number;
    longitude: number;
    missionType: string;
    started: string;
    ended: string;

    // mission ids for select
    missionTypesOptions: string[];

    // display data
    redirectToMissionsPage: boolean;
    isLoading: boolean;

    // errors
    errorMsg: string;
    nameError: boolean;
    latitudeError: boolean;
    longitudeError: boolean;
    typeError: boolean;
    startedError: boolean;
    endedError: boolean;
}

type IState = IMissionEditPageState;
type IProps = IMissionEditPageProps;

/**
 * Form for editing mission.
 *
 * @author Milos Silhar (433614)
 */
export class MissionEditPage extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            name: "",
            latitude: 0.0,
            longitude: 0.0,
            missionType: "",
            started: "",
            ended: "",
            missionTypesOptions: [],
            redirectToMissionsPage: false,
            isLoading: true,
            errorMsg: "",
            nameError: false,
            latitudeError: false,
            longitudeError: false,
            typeError: false,
            startedError: false,
            endedError: false,
        }
    }

    public componentDidMount() {
        missionService.getAllTypes().then((types) => {
            this.setState(prevState => ({...prevState, missionTypesOptions: types, isLoading: false}));
        }).catch(() => {
            const requestError = "Cannot load mission types";
            this.setState(prevState => ({...prevState, errorMsg: requestError, isLoading: false}));
        });
        missionService.getMission(this.props.missionId).then((mission) => {
            this.setState(prevState => ({...prevState,
                name: mission.name,
                latitude: mission.latitude,
                longitude: mission.longitude,
                missionType: mission.missionType,
                started: mission.started.toString(),
                ended: mission.ended.toString()
            }))
        })
    }

    /********************************************************
     * EVENT HANDLERS
     *******************************************************/
    private onNameChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const missionName = e.currentTarget.value;
        this.setState(prevState => ({...prevState, name: missionName}));
    };

    private onLatitudeChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const la = parseFloat(e.currentTarget.value);
        this.setState(prevState => ({...prevState, latitude: la}));
    };

    private onLongitudeChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const lo = parseFloat(e.currentTarget.value);
        this.setState(prevState => ({...prevState, longitude: lo}));

    };

    private onStartedChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const startedDate = e.currentTarget.value;
        this.setState(prevState => ({...prevState, started: startedDate}));
    };

    private onEndedChange = (e: ChangeEvent<HTMLInputElement>): void => {
        const endedDate = e.currentTarget.value;
        this.setState(prevState => ({...prevState, started: endedDate}));
    };

    private onMissionTypeChange = (e: ChangeEvent<HTMLSelectElement>): void => {
        const type = e.currentTarget.value;
        this.setState(prevState => ({...prevState, missionType: type}));
    };

    private onSubmit = (e: FormEvent<Button>): void => {
        e.preventDefault();

        // new request - hide previous error
        this.setState(prevState => ({...prevState,
            nameError: false,
            latitudeError: false,
            longitudeError: false,
            typeError: false,
            startedError: false,
            endedError:false }));

        // validate data
        let isFormValid = true;
        if (this.state.name.length < 1) {
            this.setState(prevState => ({...prevState, nameError: true}));
            isFormValid=false;
        }
        if (isNaN(this.state.latitude) || this.state.latitude > 90 || this.state.latitude < - 90) {
            this.setState(prevState => ({...prevState, latitudeError: true}));
            isFormValid=false;
        }
        if (isNaN(this.state.longitude) || this.state.longitude > 180 || this.state.longitude < -180) {
            this.setState(prevState => ({...prevState, longitudeError: true}));
            isFormValid=false;
        }
        if (this.state.missionType === "") {
            this.setState(prevState => ({...prevState, typeError: true}));
            isFormValid=false;
        }
        if (!this.isDate(this.state.started)) {
            this.setState(prevState => ({...prevState, startedError: true}));
            isFormValid=false;
        }
        if (!this.isDate(this.state.ended)) {
            this.setState(prevState => ({...prevState, endedError: true}));
            isFormValid=false;
        }
        if (!isFormValid) {return;}

        // create data for request
        const updatedMission = {
            id: this.props.missionId,
            name: this.state.name,
            latitude: this.state.latitude,
            longitude: this.state.longitude,
            missionType: this.state.missionType,
            started: new Date(Date.parse(this.state.started)),
            ended: new Date(Date.parse(this.state.ended))
        } as IMission;

        // save new mission and handle response
        missionService.updateMission(updatedMission).then((result) => {
            if (typeof result === "string") {
                this.setState(prevState => ({...prevState, errorMsg: result}));
            } else {
                this.setState((prevState) => ({...prevState, redirectToMissionsPage: true}))
            }
        });
    };

    private isDate(value: string): boolean {
        const msec = Date.parse(value);
        return !isNaN(msec);
    }

    public render() {
        // submit is completed or non-admin tries to access this page - redirect to missions
        if (!this.props.isAuthenticatedUserAdmin || this.state.redirectToMissionsPage) {
            return <Redirect to={`${ROUTING_URL_BASE}/missions`} />;
        }

        // form is loading
        if (this.state.isLoading) {
            return <div>Form is loading...</div>;
        }

        // server responded with error - form cannot be loaded
        if (this.state.errorMsg !== "") {
            return <Alert bsStyle={"danger"}>{this.state.errorMsg}</Alert>
        }

        return (
            <div className={'MissionNewForm'}>
                <h2>Create new mission</h2>
                <form className={'MissionNewForm__form'}>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"nameInput"}>Name</label>
                            <input type={"text"} className={this.state.nameError ? "form-control is-invalid" : "form-control is-valid"}
                                   id={"nameInput"} placeholder={"Name"} value={this.state.name} onChange={this.onNameChange}/>
                            <div className={this.state.nameError ? "invalid-feedback" : "valid-feedback"}>
                                {this.state.nameError ? "Name is required" : "OK"}
                            </div>
                        </div>
                    </div>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"latitudeInput"}>Latitude</label>
                            <input type={"number"} className={this.state.latitudeError ? "form-control is-invalid" : "form-control is-valid"}
                                   id={"latitudeInput"} placeholder={"Latitude"} value={this.state.latitude} onChange={this.onLatitudeChange}/>
                            <div className={this.state.latitudeError ? "invalid-feedback" : "valid-feedback"}>
                                {this.state.latitudeError ? "Latitude must be a number between -90 and 90!" : "OK"}
                            </div>
                        </div>
                    </div>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"longitudeInput"}>Longitude</label>
                            <input type={"number"}  className={this.state.longitudeError ? "form-control is-invalid" : "form-control is-valid"}
                                   id={"longitudeInput"} placeholder={"Longitude"} value={this.state.longitude} onChange={this.onLongitudeChange}/>
                            <div className={this.state.longitudeError ? "invalid-feedback" : "valid-feedback"}>
                                {this.state.longitudeError ? "Longitude must be a number between -180 and 180!" : "OK"}
                            </div>
                        </div>
                    </div>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"typeSelect"}>Mission Type</label>
                            <select className={"form-control"} id={"typeSelect"} value={this.state.missionType} onChange={this.onMissionTypeChange}>
                                {this.state.missionTypesOptions.map((typeOption) =>
                                    <option key={typeOption}
                                            value={typeOption}>{typeOption}</option>
                                )}
                            </select>
                        </div>
                    </div>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"startedInput"}>Start date</label>
                            <input type={"text"}  className={this.state.startedError ? "form-control is-invalid" : "form-control is-valid"}
                                   id={"startedInput"} placeholder={"Start date"} value={this.state.started} onChange={this.onStartedChange}/>
                            <div className={this.state.startedError ? "invalid-feedback" : "valid-feedback"}>
                                {this.state.startedError ? "Invalid Date (required format: yyyy-MM-dd)" : "OK"}
                            </div>
                        </div>
                    </div>
                    <div className={"form-row"}>
                        <div className={"col-md-6 mb-3"}>
                            <label htmlFor={"endedInput"}>End date</label>
                            <input type={"text"}  className={this.state.endedError ? "form-control is-invalid" : "form-control is-valid"}
                                   id={"endedInput"} placeholder={"End date"} value={this.state.ended} onChange={this.onEndedChange}/>
                            <div className={this.state.endedError ? "invalid-feedback" : "valid-feedback"}>
                                {this.state.endedError ? "Invalid Date (required format: yyyy-MM-dd)" : "OK"}
                            </div>
                        </div>
                    </div>
                </form>

                <Button bsStyle={"primary"} className={"mr-2"} type={"submit"} onClick={this.onSubmit}>Submit</Button>
                <Link className={"btn btn-dark"} to={`${ROUTING_URL_BASE}/missions`}>Cancel</Link>
            </div>
        )
    }
}