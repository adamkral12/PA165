import * as React from "react";
import {getAllAgents} from "../../services/agentService";
import {IAgent} from "../../types/Agent";

interface IAgentsState {
    agents: IAgent[]
}

export class AgentsPage extends React.Component<any, IAgentsState> {
    constructor(props: any) {
        super(props);

        getAllAgents().then(
            response => {
                const agents = response.data as IAgent[];
                this.setState({agents});
            }
        )
    }

    public render() {
        if (this.state) {
            const tableRows = this.state.agents.map(agent =>
                <tr key={agent.id}>
                    <td>{agent.name}</td>
                    <td>{agent.birthDate}</td>
                    <td>{agent.languages}</td>
                    <td>{agent.rank}</td>
                </tr>
            );
            return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Birth Date</th>
                                <th>Languages</th>
                                <th>Rank</th>
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
