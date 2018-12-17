import * as React from "react";
import {FormEvent} from "react";
import * as reportService from "../../services/reportService";
import {RouteComponentProps} from "react-router";
import {IReport} from "../../types/Report";
import "./ReportDetail.css";
import {Alert, Button, FormControl, FormGroup} from "react-bootstrap";
import {Link} from "react-router-dom";
import {ROUTING_URL_BASE} from "../../utils/requestUtils";
import {AlertCloseable} from "../alert-closeable/AlertCloseable";
import {IAgent} from "../../types/Agent";

// easiest way, how to add image to page
const topSecretImgSrc = "https://vignette.wikia.nocookie.net/uncyclopedia/images/2/25/Top_Secret_Stamp.png" +
                        "/revision/latest?cb=20061106174706";

interface IReportDetailState {
    readonly isLoading: boolean;
    readonly loadingError: string;
    readonly text: string;
    readonly submitError: string;
    readonly serverError: string;
    readonly wasSaved: boolean;
    readonly mentionedAgentsInReport: IAgent[] | null;
}

type IProps = RouteComponentProps<{reportId: string}>;
type IState = IReportDetailState;

/**
 * Detail view for report. Is used for both editing and displaying details of report.
 *
 * @author Jan Pavlu
 */
export class ReportDetail extends React.PureComponent<IProps, IState> {

    private displayedReport: IReport;

    /********************************************************
     * EVENT HANDLERS
     *******************************************************/
    private onTextChange = (e: FormEvent<FormControl>): void => {
        const text = (e.target as HTMLTextAreaElement).value;
        this.setState(prevState => ({...prevState, text , submitError: ""}));
    };

    private onFindMentionedAgents = async () => {
        // already run
        if (this.state.mentionedAgentsInReport != null) {
            return;
        }
        const response = await reportService.getAgentsMentionedInReport(this.displayedReport.id);
        if (typeof response === "string") {
            this.setState(prevState => ({...prevState, serverError: response}));
            return;
        }

        this.setState(prevState => ({...prevState, mentionedAgentsInReport: response}));
    };

    private onSaveClicked = async (): Promise<void> => {
        if (this.state.text.trim().length < 10) {
            this.setState(prevState => ({...prevState, submitError: "Text is too short"}));
            return;
        }

        // nothing was changed
        if (this.state.text.trim() === this.displayedReport.text) {
            return;
        }

        const updateReportData = {id: this.displayedReport.id, text: this.state.text};

        this.setState(prevState => ({...prevState, isLoading: true, wasSaved: false}));
        const response = await reportService.updateReport(updateReportData);

        // response is error
        if (typeof response === "string") {
            this.setState(prevState => ({...prevState, serverError: response, isLoading: false}));
            return;
        }

        // response is updated report
        this.displayedReport = response;
        this.setState(prevState => ({...prevState, text: response.text, isLoading: false, wasSaved: true}));
    };

    private onHideServerError = (): void => {
        this.setState(prevState => ({...prevState, serverError: ""}));
    };

    /********************************************************
     * LIFE-CYCLE METHODS
     *******************************************************/
    constructor(props: IProps) {
        super(props);

        this.state = {
            isLoading: true,
            loadingError: "",
            text: "", // used if detail is in editing mode
            submitError: "",
            serverError: "",
            wasSaved: false,
            mentionedAgentsInReport: null,
        }
    }

    public async componentDidMount(): Promise<void> {
        await this.loadReport(parseInt(this.props.match.params.reportId, 10));
    }

    private async loadReport(reportId: number): Promise<void> {
        const response = await reportService.getReportById(reportId);
        if (typeof response === "string") {
            this.setState(prevState => ({...prevState, loadingError: response, isLoading: false}));
            return;
        }
        this.displayedReport = response;
        this.setState(prevState => ({...prevState, text: response.text, isLoading: false}));
    }

    private getFindAgentsResult(): null | JSX.Element |JSX.Element[] {
        if (this.state.mentionedAgentsInReport == null) {
            return null;
        }

        if (this.state.mentionedAgentsInReport.length === 0) {
            return <Alert bsStyle={"info"}>No agents found</Alert>;
        }

        return this.state.mentionedAgentsInReport.map((agent) => (
            <div className={"ReportDetail__findAgentsResult"} key={agent.id}>{agent.codeName}</div>
        ));
    }

    /********************************************************
     * RENDERING
     *******************************************************/
    private isEditingMode(): boolean {
        return this.props.location.pathname.indexOf("edit") !== -1;
    }

    public render(): JSX.Element {
        if (this.state.isLoading) {
            return <div>Detail is loading</div>;
        }

        if (this.state.loadingError !== "") {
            return <Alert bsStyle={'danger'}>{this.state.loadingError}</Alert>
        }

        const textComponent = this.isEditingMode() ? (
            (<FormGroup controlId={'ReportDetail__text'}>
                <FormControl componentClass={'textarea'}
                             value={this.state.text}
                             placeholder={'Report must contain at least 10 characters'}
                             onChange={this.onTextChange}/>
            </FormGroup>)
        ) : <div className={"ReportDetail__text"}>{this.displayedReport.text}</div>;

        return (
            <div className={"ReportDetail"}>

                <AlertCloseable isVisible={this.state.serverError !== ""}
                                bsStyle={'danger'}
                                onHide={this.onHideServerError}/>

                <Button className={"btn-secondary"}>
                    <Link to={`${ROUTING_URL_BASE}/reports`}>Back to reports</Link>
                </Button>
                <hr />

                <h3>Detail of report {this.props.match.params.reportId}</h3>
                <div className={"ReportDetail__content-top"}>
                    <div className={"ReportDetail__baseData"}>
                        <div>Mission: <em className={"text-primary"}>{this.displayedReport.mission.name}</em></div>
                        <div>Created at: <em>{this.displayedReport.date}</em></div>
                        <div>Result: <em>{this.displayedReport.missionResult}</em></div>
                        <div>Status: <em>{this.displayedReport.reportStatus}</em></div>
                    </div>
                    <img title={'Top-secret'} src={topSecretImgSrc} alt={'Top secret logo'} className={"ReportDetail__img"}/>
                </div>
                <div className={"ReportDetail__textWrapper"}>Text: {textComponent}</div>
                {this.state.wasSaved && <p className={"text-success"}>Report was successfully updated</p>}
                {this.isEditingMode() && <Button bsStyle={"primary"} onClick={this.onSaveClicked}>Save</Button>}
                {this.getFindAgentsResult()}
                {!this.isEditingMode() && (
                    <Button className={"ReportDetail__findAgentsButton"}
                            bsStyle={"warning"} onClick={this.onFindMentionedAgents}>
                        Find mentioned agents
                    </Button>
                )}
            </div>
        )
    }

}
