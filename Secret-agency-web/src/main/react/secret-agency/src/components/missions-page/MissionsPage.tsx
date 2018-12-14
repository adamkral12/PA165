import * as React from "react";
import "../SharedStyles.css";
import {IMission} from "../../types/Mission";
import {getAllMissions} from "../../services/missionService";

interface IMissionsState {
    missions: IMission[]
}

export class MissionsPage extends React.Component<any, IMissionsState> {
    constructor(props: any) {
        super(props);

        // TODO finish after RestController is ready
        getAllMissions().then(
            response => {
                const missions = response.data as IMission[];
                this.setState({missions});
            }
        )
    }

    public render() {
        if (this.state) {
            const tableRows = this.state.missions.map(mission =>
                <tr key={mission.id}>
                    <td>{mission.name}</td>
                </tr>
            );
            return (
                <div className="table-wrapper">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Name</th>
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
