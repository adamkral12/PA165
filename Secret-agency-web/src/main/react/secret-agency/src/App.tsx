import * as React from 'react';
import {MouseEvent} from 'react';
import './App.css';
import {LoginPage} from "./components/LoginForm";
import * as authenticationService from "./services/authenticationService";
import * as secretAgencyRepository from "./repository/secretAgecyRepository";
import {IAgent} from "./types/Agent";
import {TopBar} from "./components/top-bar/TopBar"
import {AgentsPage} from "./components/agents-page/AgentsPage";
import { Route, BrowserRouter } from "react-router-dom";
import {DepartmentsPage} from "./components/departments-page/DepartmentsPage";

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
                {title: "Agents", link: "agents"},
                {title: "Missions", link: "missions"},
                {title: "Departments", link: "departments"},
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

        return (
            <div className="App">
                <TopBar tabs={this.state.tabs} logout={this.onLogout}/>
                <button className="logout-button" type={'button'} onClick={this.onLogout}>Log out</button>
                <BrowserRouter>
                    <Route path="/agents" component={AgentsPage}/>
                </BrowserRouter>
                <BrowserRouter>
                    <Route path="/departments" component={DepartmentsPage}/>
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
