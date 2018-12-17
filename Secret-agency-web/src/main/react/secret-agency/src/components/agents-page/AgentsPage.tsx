import * as React from "react";
import "../SharedStyles.css";
import "./AgentsPage.css"
import {editAgent, getAgentRanks, getAllAgents, getAllLanguages} from "../../services/agentService";
import {IAgent} from "../../types/Agent";

interface IAgentsState {
    readonly agents: IAgent[]
    readonly newAgent: INewAgent
    readonly ranks: string[]
    readonly languages: string[]
    readonly edit: boolean
}

export interface INewAgent {
    name: string,
    birthDate: string,
    languages: string[],
    rank: string
    id: number,
}

export class AgentsPage extends React.Component<any, IAgentsState> {
    constructor(props: any) {
        super(props);
    }

    public componentDidMount() {
        this.loadData();
    }

    private loadData() {
        getAllAgents().then(
            response => {
                const agents = response.data as IAgent[];
                const newAgent = {
                    name: "",
                    birthDate: "",
                    languages: [],
                    rank: "",
                    id: 1,
                };
                this.setState({
                    agents,
                    newAgent,
                    edit: false
                });
            }
        );
        getAgentRanks().then(
            response => {
                const ranks = response.data as string[];
                this.setState({ranks});
            }
        );
        getAllLanguages().then(
            response => {
                const languages = response.data as string[];
                this.setState({languages});
            }
        );
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
                    codeName: agent.codeName,
                    departmentId: agent.department.id,
                };
                this.setState({
                    newAgent,
                    edit: true
                });
            }
        });
    }

    private updateNewAgent(value: string, prop: string) {
        this.state.newAgent[prop] = value;
    }

    private saveEditedAgent() {
        editAgent(this.state.newAgent).then(
            editedAgent => {
                const agents = this.state.agents;
                agents.forEach((agent, index) => {
                    if (agent.id === editedAgent.id) {
                        agents[index] = editedAgent;
                    }
                });
                this.setState({agents});
                this.clearEditRow();
            }, () => {
                this.clearEditRow();
            }
        );
    }

    private clearEditRow() {
        const newAgent = {
            name: "",
            birthDate: "",
            languages: [],
            rank: "",
            id: 1,
        };
        this.setState({
            newAgent,
            edit: false
        });
    }

    public render() {
        if (this.state && this.state.agents && this.state.languages && this.state.ranks) {
            const tableRows = this.state.agents.map(agent =>
                <tr key={agent.id}>
                    <td>{agent.name}</td>
                    <td>{agent.birthDate}</td>
                    <td>{agent.languages}</td>
                    <td>{agent.rank}</td>
                    <td>
                        <button className="btn btn-primary edit-button" onClick={() => this.editAgent(agent.id)}>Edit</button>
                    </td>
                </tr>
            );
            return (
                <div className="table-wrapper">
                    <table className="data-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Birth Date</th>
                                <th>Languages</th>
                                <th>Rank</th>
                                <th className="table-row-width">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tableRows}
                            {this.state.edit && (
                                <tr key={this.state.newAgent.id}>
                                    <td><input type="text" defaultValue={this.state.newAgent.name} onChange={(evt) => this.updateNewAgent(evt.target.value, "name")}/></td>
                                    <td><input type="text" defaultValue={this.state.newAgent.birthDate} onChange={(evt) => this.updateNewAgent(evt.target.value, "birthDate")}/></td>
                                    <td>
                                        <select defaultValue={this.state.newAgent.languages[0] ? this.state.newAgent.languages[0] : ""} onChange={(evt) => this.updateNewAgent(evt.target.value, "languages")}
                                        >
                                            <option value={""}/>
                                            {this.state.languages.map((language: string) =>
                                                <option key={language}
                                                        value={language}>{language}</option>
                                            )}
                                        </select>
                                    </td>
                                    <td>
                                        <select defaultValue={this.state.newAgent.rank} onChange={(evt) => this.updateNewAgent(evt.target.value, "rank")}
                                        >
                                            <option value={""}/>
                                            {this.state.ranks.map((rank: string) =>
                                                <option key={rank}
                                                        value={rank}>{rank}</option>
                                            )}
                                        </select>
                                    </td>
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
