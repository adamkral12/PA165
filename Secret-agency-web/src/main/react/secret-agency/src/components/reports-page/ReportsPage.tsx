import * as React from "react";
import {IReport, ReportStatus} from "../../types/Report";
import * as reportService from "../../services/reportService";
import {Link, Redirect} from "react-router-dom";
import {RouteComponentProps} from "react-router";
import {ROUTING_URL_BASE} from "../../utils/requestUtils";
import {Button} from "react-bootstrap";
import "./ReportsPage.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPencilAlt, faSearch, faTrash} from '@fortawesome/free-solid-svg-icons'
import {AlertCloseable} from "../alert-closeable/AlertCloseable";
import {ReportFilter} from "./ReportFilter";

type NavigateToDetailMode = "EDIT" | "READ-ONLY";

interface IReportsPageOwnProps {
    isAuthenticatedUserAdmin: boolean;
    authenticatedUserId: number;
}

interface IReportPageState {
    reports: IReport[],
    isLoaded: boolean,
    navigateToDetailId: number,
    navigateToDetailMode: NavigateToDetailMode;
    serverError: string,
}

type IState = IReportPageState;
type IProps = IReportsPageOwnProps & RouteComponentProps<{}>;

/**
 * Component for reports page.
 *
 * @author Jan Pavlu (487548)
 */
export class ReportsPage extends React.PureComponent<IProps, IState> {

    /********************************************************
     * EVENT HANDLERS
     *******************************************************/
    private onRemoveReport = async (reportId: number): Promise<void> => {
        const wasDeleted = await reportService.deleteReport(reportId);
        if (wasDeleted) {
            const reports = this.state.reports.filter((report) => report.id !== reportId);
            this.setState(prevState => ({...prevState, reports}));
        }
    };

    private onShowDetail = (reportId: number): void => {
        this.setState(prevState => ({...prevState, navigateToDetailId: reportId,
            navigateToDetailMode: "READ-ONLY"}));
    };

    private onEditReport = (reportId: number): void => {
        this.setState(prevState => ({...prevState, navigateToDetailId: reportId, navigateToDetailMode:"EDIT"}))
    };

    private onApproveReport = async (reportId: number): Promise<void> => {
        const result = await reportService.approveReport(reportId);
        // error
        if (typeof result === "string") {
            this.setState(prevState => ({...prevState, serverError: result}));
            return;
        }

        // success
        this.updateReportStatus(reportId, "APPROVED");
    };

    private onDenyReport = async (reportId: number): Promise<void> => {
        const result = await reportService.denyReport(reportId);
        // error
        if (typeof result === "string") {
            this.setState(prevState => ({...prevState, serverError: result}));
            return;
        }

        // success
        this.updateReportStatus(reportId, "DENIED");
    };

    private updateReportStatus(reportId: number, reportStatus: ReportStatus) {
        const updatedReports = this.state.reports.map((report) => {
            if (report.id === reportId) {
                return {...report, reportStatus};
            }
            return report;
        });

        this.setState(prevState => ({...prevState, reports: updatedReports}));
    }

    private onHideServerError = (): void => {
        this.setState(prevState => ({...prevState, serverError: ""}));
    };

    private onFilterResult = (result: IReport[] | string) => {
        if (typeof result === "string") {
            this.setState(prevState => ({...prevState, serverError: result}));
            return;
        }

        this.setState(prevState => ({...prevState, reports: result}));
    };

    /********************************************************
     * LIFE-CYCLE METHODS
     *******************************************************/
    constructor(props: IProps) {
        super(props);

        this.state = {
            reports: [],
            isLoaded: false,
            navigateToDetailId: -1, // -1 means don't navigate
            navigateToDetailMode: "READ-ONLY",
            serverError: "",
        }
    }

    public componentDidMount() {
        reportService.getAllReports().then((reports) => {
            const displayedReports = this.filterReports(reports);
            this.setState((prevState) => ({...prevState, isLoaded: true, reports: displayedReports}));
        });
    }

    /********************************************************
     * RENDERING
     *******************************************************/
    /**
     * Admin can view all reports. User can view only his own reports. Returns filtered reports.
     * @param reports reports
     */
    private filterReports(reports: IReport[]): IReport[] {
        if (!this.props.isAuthenticatedUserAdmin) {
            return reports.filter((report) => report.agent.id === this.props.authenticatedUserId);
        }
        return reports;
    }
    
    private getActionsForReport(report: IReport) {
        const canUserEdit = report.agent.id === this.props.authenticatedUserId && report.reportStatus !== "APPROVED";
        if (canUserEdit || this.props.isAuthenticatedUserAdmin) {
            return (
                <div className={'ReportsPage__actions'}>
                    <span className={"icon-operation"} onClick={() => this.onShowDetail(report.id)}>
                        <FontAwesomeIcon icon={faSearch} size={"2x"} />
                    </span>
                    <span className={"icon-operation"} onClick={() => this.onEditReport(report.id)}>
                        <FontAwesomeIcon icon={faPencilAlt} size={"2x"} />
                    </span>
                    <span className={"icon-operation"} onClick={() => this.onRemoveReport(report.id)}>
                        <FontAwesomeIcon icon={faTrash} size={"2x"} />
                    </span>
                </div>
            );
        }

        // authenticated agent is not admin or Report author
        return (
            <div className={'ReportsPage__actions'}>
                <span className={"icon-operation"} onClick={() => this.onShowDetail(report.id)}>
                    <FontAwesomeIcon icon={faSearch} size={"2x"} />
                </span>
            </div>
        );
    }

    private getApproveDenyActionsForReport(report: IReport): JSX.Element | null {
        if (!this.props.isAuthenticatedUserAdmin) {
            return null;
        }

        if (report.reportStatus === "APPROVED" || report.reportStatus === "DENIED") {
            return <small>Already done</small>;
        }

        return (
            <div className={"ReportsPage__approveDeny"}>
                <Button bsStyle={"success"} onClick={() => this.onApproveReport(report.id)}>Approve</Button>
                <Button bsStyle={"danger"} onClick={() => this.onDenyReport(report.id)}>Deny</Button>
            </div>
        )
    }

    private getRedirectUrl(): string {
        const currentPath = this.props.match.path;
        const detailId = this.state.navigateToDetailId;
        const mode = this.state.navigateToDetailMode;

        const urlBase = `${currentPath}/report/${detailId}`;
        return mode === "READ-ONLY" ? urlBase : urlBase + `/edit`;

    }

    public render(): JSX.Element {
        const detailId = this.state.navigateToDetailId;
        // go to detail
        if (detailId !== -1) {
            return <Redirect to={this.getRedirectUrl()}/>;
        }

        if (!this.state.isLoaded) {
           return <div>Loading table...</div>;
        }

        const isAnyRowVisible = this.state.reports.length !== 0;

        const tableRows = this.state.reports.map((report) => (
            <tr key={report.id}>
                <td>{this.getActionsForReport(report)}</td>
                <td>{report.mission && report.mission.name}</td>
                {this.props.isAuthenticatedUserAdmin && <td>{report.agent.codeName}</td>}
                <td>{report.date}</td>
                <td>{report.missionResult}</td>
                <td>{report.reportStatus}</td>
                {this.getApproveDenyActionsForReport(report) != null &&
                    <td>{this.getApproveDenyActionsForReport(report)}</td>}
            </tr>
           )
        );

        return (
            <div className={'ReportsPage'}>
                {!isAnyRowVisible ? (
                    <div className={'alert alert-info'}>
                        There are no reports to display...
                    </div>)
                    : (
                    <div className={"table-wrapper"}>
                        <table className={"data-table"}>
                            <thead>
                            <tr>
                                <th>Actions</th>
                                <th>Mission</th>
                                {this.props.isAuthenticatedUserAdmin && <th>Agent</th>}
                                <th>Date</th>
                                <th>Mission result</th>
                                <th>Report status</th>
                                {this.props.isAuthenticatedUserAdmin &&<th>Approve/deny</th>}
                            </tr>
                            </thead>
                            <tbody>
                            {tableRows}
                            </tbody>
                        </table>
                    </div>
                )}
                <AlertCloseable isVisible={this.state.serverError !== ""}
                                bsStyle={"danger"}
                                onHide={this.onHideServerError}>
                    {this.state.serverError}
                </AlertCloseable>

                <Button bsStyle="primary" className={'ReportsPage__createNewReport'}>
                    <Link to={`${ROUTING_URL_BASE}/reports/new`}>Create report</Link>
                </Button>


                <ReportFilter onSearch={this.onFilterResult}/>
            </div>
        );
    }
}
