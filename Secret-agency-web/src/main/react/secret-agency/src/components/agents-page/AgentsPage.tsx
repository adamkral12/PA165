import * as React from "react";
import {getAllAgents} from "../../services/agentService";

export class AgentsPage extends React.Component<any, any> {
    constructor(props) {
        super(props);

        getAllAgents().then(
            response => {
                this.setState({agents: response.data});
            }
        )
    }

    public render() {
        if (this.state && this.state.agents !== null) {
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
