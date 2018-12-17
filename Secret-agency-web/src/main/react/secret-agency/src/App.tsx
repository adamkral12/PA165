import * as React from 'react';
import {MouseEvent} from 'react';
import './App.css';
import {LoginPage} from "./components/LoginForm";
import * as authenticationService from "./services/authenticationService";
import * as secretAgencyRepository from "./repository/secretAgecyRepository";
import {IAgent} from "./types/Agent";
import {TopBar} from "./components/top-bar/TopBar"
import {AgentsPage} from "./components/agents-page/AgentsPage";
import {BrowserRouter, Redirect, Route} from "react-router-dom";
import {DepartmentsPage} from "./components/departments-page/DepartmentsPage";
import {ROUTING_URL_BASE} from "./utils/requestUtils";
import {ReportsPage} from "./components/reports-page/ReportsPage";
import {ReportNewForm} from "./components/reports-page/ReportNewForm";
import {MissionsPage} from "./components/missions-page/MissionsPage";
import {MissionNewPage} from "./components/missions-page/MissionNewPage";
import {ReportDetail} from "./components/reports-page/ReportDetail";
import {MissionEditPage} from "./components/missions-page/MissionEditPage";
import {MissionDetailPage} from "./components/missions-page/MissionDetailPage";

export interface ITab {
    title: string,
    link: string
}

interface IAppState {
    loginError: string;
    authenticatedAgent: IAgent | null;
    tabs: ITab[];
}

type IState = IAppState;

/**
 * Main component. Shows either login form, or home page.
 */
class App extends React.Component<{}, IState> {

    /********************************************************
     * EVENT HANDLERS
     *******************************************************/
    /**
     * Authentication method. Try to authenticate agent with given credentials.
     * @param codeName codename
     * @param password password
     */
    private onLogin = (codeName: string, password: string): void => {
        authenticationService.authenticate(codeName, password)
            .then((response) => {
                // error was returned
                if (typeof response === "string") {
                    this.setState((prevState) => ({...prevState, loginError: response}));
                } else {
                    // put authenticated user to state and local storage (to enable automatic authentication)
                    this.setState((prevState) => ({...prevState, authenticatedAgent: response}));
                    secretAgencyRepository.storeAuthenticatedAgent(response);
                }
            })
    };

    private onLogout = (e: MouseEvent<HTMLButtonElement>): void => {
        e.preventDefault();
        authenticationService.logOut();
        this.setState((prevState) => ({...prevState, authenticatedAgent: null}));
        secretAgencyRepository.removeAuthenticatedAgent();
    };

    /**
     * Hide errors when user starts to type.
     */
    private hideAuthenticationError = (): void => {
        this.setState((prevState) => ({...prevState, loginError: ""}))
    };

    /********************************************************
     * LIFE-CYCLE METHODS
     *******************************************************/
    constructor(props: {}) {
        super(props);

        this.state = {
            authenticatedAgent: null,
            loginError: "",
            tabs: [
                {title: "Agents", link: `${ROUTING_URL_BASE}/agents`},
                {title: "Missions", link: `${ROUTING_URL_BASE}/missions`},
                {title: "Departments", link: `${ROUTING_URL_BASE}/departments`},
                {title: "Reports", link: `${ROUTING_URL_BASE}/reports`},
            ]
        }
    }

    public componentDidMount(): void {
        const authenticatedAgent = authenticationService.authenticateAutomatically();
        if (authenticatedAgent != null) {
            this.setState((prevState) => ({...prevState, authenticatedAgent}))
        }
    }

    /********************************************************
     * RENDERING
     *******************************************************/
    public render(): JSX.Element {
        const isLoggedIn = this.state.authenticatedAgent != null;

        if (!isLoggedIn) {
            return <LoginPage onLogin={this.onLogin}
                              loginError={this.state.loginError}
                              hideAuthenticationError={this.hideAuthenticationError} />
        }

        const isAuthenticatedAgentAdmin = this.state.authenticatedAgent!.rank === "AGENT_IN_CHARGE";
        const authenticatedAgentId = this.state.authenticatedAgent!.id;

        return (
            <BrowserRouter>
                <div className="App">
                    <div className={"header"}>
                        <TopBar tabs={this.state.tabs} logout={this.onLogout}/>
                        <button className="logout-button btn btn-danger" type={'button'} onClick={this.onLogout}>Log out</button>
                    </div>
                    <div className={"content"}>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/agents`} component={AgentsPage} />
                        <Route exact={true} path={`${ROUTING_URL_BASE}/departments`} component={DepartmentsPage}/>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/missions`} render={(props) => (
                            <MissionsPage {...props}
                                         authenticatedUserId={authenticatedAgentId}
                                         isAuthenticatedUserAdmin={isAuthenticatedAgentAdmin}/>
                        )}/>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/missions/new`} render={(props) => (
                            <MissionNewPage {...props}
                                            isAuthenticatedUserAdmin={isAuthenticatedAgentAdmin}
                                            authenticatedUserId={authenticatedAgentId}/>
                        )}/>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/missions/edit/:missionId`} render={(props) => (
                            <MissionEditPage {...props}
                                             isAuthenticatedUserAdmin={isAuthenticatedAgentAdmin}
                                             missionId={props.match.params.missionId}/>
                        )}/>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/missions/detail/:missionId`} render={(props) => (
                            <MissionDetailPage {...props}
                                               isAuthenticatedUserAdmin={isAuthenticatedAgentAdmin}
                                               missionId={props.match.params.missionId}/>
                        )}/>
                        <Route exact={true} path={`${ROUTING_URL_BASE}/reports`} render={(props) => (
                            <ReportsPage {...props}
                                         authenticatedUserId={authenticatedAgentId}
                                         isAuthenticatedUserAdmin={isAuthenticatedAgentAdmin}/>
                        )}/>
                        <Route exact path={`${ROUTING_URL_BASE}/reports/new`} render={(props) => (
                            <ReportNewForm {...props} authenticatedAgentId={authenticatedAgentId}/>
                        )} />
                        <Route path={`${ROUTING_URL_BASE}/reports/report/:reportId`} component={ReportDetail}/>
                    </div>
                    <Redirect to={`${ROUTING_URL_BASE}/reports`}/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
