import * as React from "react";
import "../SharedStyles.css";
import "./AgentsPage.css"
import {editAgent, getAgentRanks, getAllAgents, getAllLanguages} from "../../services/agentService";
import {IAgent} from "../../types/Agent";

interface IAgentsState {
    readonly agents: IAgent[];
    readonly newAgent: INewAgent;
    readonly ranks: string[];
    readonly languages: string[];
    readonly edit: boolean;
    readonly formErrors: string[];
}

export interface INewAgent {
    readonly name: string;
    readonly birthDate: string;
    readonly languages: string[];
    readonly rank: string;
    readonly id: number;
    readonly codeName: string;
}

export class AgentsPage extends React.Component<any, IAgentsState> {
    public async componentDidMount() {
        this.loadData();
    }

    private async loadData() {
        // get async data
        const agents = await getAllAgents();
        const ranks = await getAgentRanks();
        const languages = await getAllLanguages();

        // set default properties
        const newAgent = {
            name: "",
            birthDate: "",
            languages: [],
            rank: "",
            id: 1,
            codeName: ""
        };

        this.setState(_ => ({
            agents,
            newAgent,
            edit: false,
            ranks,
            languages,
            formErrors: [],
        }));
    }

    private editAgent(id: number) {
        this.state.agents.forEach(agent => {
            if (agent.id === id) {
                this.clearEditRow();
                const newAgent = {
                    name: agent.name,
                    birthDate: agent.birthDate.toString(),
                    languages: agent.languages,
                    rank: agent.rank,
                    id: agent.id,
                    departmentId: agent.department.id,
                    codeName: agent.codeName,
                };
                console.log(newAgent);
                this.setState({
                    newAgent,
                    edit: true
                });
            }
        });
    }

    private updateNewAgent(value: string, prop: string) {
        if (prop === "languages") {
            this.setState((prevState) => ({newAgent: {...prevState.newAgent, languages: value.split(",")}}));
            return;
        }
        this.state.newAgent[prop] = value;
    }

    private saveEditedAgent() {
        if (this.validate().length > 0) {
            this.setState(_ => ({formErrors: this.validate()}));
        } else {
            editAgent(this.state.newAgent).then(
                editedAgent => {
                    const agents = this.state.agents;
                    agents.forEach((agent, index) => {
                        if (agent.id === editedAgent.id) {
                            agents[index] = editedAgent;
                        }
                    });
                    this.setState({agents, formErrors: []});
                    this.clearEditRow();
                }, () => {
                    this.clearEditRow();
                }
            );
        }
    }

    private validate = (): string[] => {
        const errors = [];
        if (!/([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])$/.test(this.state.newAgent.birthDate)) {
            errors.push("Invalid date format");
        }

        if (!this.state.newAgent.name.trim()) {
            errors.push("Name cannot be empty");
        }

        if (!this.state.newAgent.codeName.trim()) {
            errors.push("Code name cannot be empty");
        }

        this.state.newAgent.languages.forEach(language => {
            if (this.state.languages.indexOf(language) === -1) {
                errors.push("Invalid language " + language);
            }
        });
        return errors
    };

    private clearEditRow() {
        const newAgent = {
            name: "",
            birthDate: "",
            languages: [],
            rank: "",
            id: 1,
            codeName: ""
        };
        this.setState({
            newAgent,
            edit: false
        });
    }

    public render() {
        if (this.state) {
            const tableRows = this.state.agents.map(agent =>
                <tr key={agent.id}>
                    <td>{agent.name}</td>
                    <td>{agent.birthDate}</td>
                    <td>{agent.languages.join(", ")}</td>
                    <td>{agent.rank}</td>
                    <td>{agent.codeName}</td>
                    <td>
                        <button className="btn btn-primary edit-button" onClick={() => this.editAgent(agent.id)}>Edit</button>
                    </td>
                </tr>
            );
            return (
                <div className="table-wrapper">
                    {this.state.formErrors.length > 0 && <div className={'alert alert-danger'}>
                        {this.state.formErrors.map((error: string, index) =>
                            <p key={index}>{error}</p>
                        )}
                    </div>}
                    <table className="data-table">
                        <thead>
                            <tr className="table-row-width">
                                <th>Name</th>
                                <th>Birth Date</th>
                                <th>Languages</th>
                                <th>Rank</th>
                                <th>Code Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tableRows}
                            {this.state.edit && (
                                <tr key={this.state.newAgent.id}>
                                    <td><input type="text" defaultValue={this.state.newAgent.name} onChange={(evt) => this.updateNewAgent(evt.target.value, "name")}/></td>
                                    <td><input type="text" defaultValue={this.state.newAgent.birthDate} onChange={(evt) => this.updateNewAgent(evt.target.value, "birthDate")}/></td>
                                    <td><input type="text" defaultValue={this.state.newAgent.languages.join(",")} onChange={(evt) => this.updateNewAgent(evt.target.value, "languages")}/></td>
                                    <td>
                                        <select defaultValue={this.state.newAgent.rank} onChange={(evt) => this.updateNewAgent(evt.target.value, "rank")}
                                        >
                                            {this.state.ranks.map((rank: string) =>
                                                <option key={rank}
                                                        value={rank}>{rank}</option>
                                            )}
                                        </select>
                                    </td>
                                    <td><input type="text" defaultValue={this.state.newAgent.codeName} onChange={(evt) => this.updateNewAgent(evt.target.value, "codeName")}/></td>
                                    <td>
                                        <button className={"btn btn-primary save-button"} onClick={() => this.saveEditedAgent()}>Save</button>
                                        <button className={"btn btn-info cancel-button"} onClick={() => this.clearEditRow()}>Cancel</button>
                                    </td>
                                </tr>
                            )}
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
