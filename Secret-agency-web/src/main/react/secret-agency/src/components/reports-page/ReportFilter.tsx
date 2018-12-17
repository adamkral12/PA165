import * as React from "react";
import {Alert, Button, FormControl, FormGroup, InputGroup} from "react-bootstrap";
import {FormEvent} from "react";
import "./ReportFilter.css";
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import * as moment from "moment";
import {IReport} from "../../types/Report";
import * as reportService from "../../services/reportService";

const SEARCH_BY_RESULT = "mission result";
const SEARCH_BY_STATUS = "report status";
const SEARCH_BY_INTERVAL = "interval";
const SEARCH_BY_EMPTY = "--reset--";

const filters  = [
    SEARCH_BY_RESULT,
    SEARCH_BY_STATUS,
    SEARCH_BY_INTERVAL,
    SEARCH_BY_EMPTY,
];

const REPORT_RESULTS = ["COMPLETED", "FAILED"];
const REPORT_STATUSES = ["NEW", "UPDATED", "APPROVED", "DENIED"];

type SearchBy = typeof SEARCH_BY_RESULT | typeof SEARCH_BY_STATUS | typeof SEARCH_BY_INTERVAL | typeof SEARCH_BY_EMPTY;

interface IReportFilterState {
    filterBy: SearchBy;
    reportResult: string;
    reportStatus: string;
    dateFrom: string;
    dateTo: string;
    formError: string;
}

interface IReportFilterDispatchProps {
    readonly onSearch: (reports: IReport[] | string) => void;
}

type IState = IReportFilterState;
type IProps = IReportFilterDispatchProps;

/**
 * Basic filtering for reports.
 *
 * @author Jan Pavlu (487548)
 */
export class ReportFilter extends React.PureComponent<IProps, IState> {

    /********************************************************
     * EVENT HANDLERS
     *******************************************************/
    private onSearchByChanged = (e: FormEvent<FormControl>): void => {
        const filterBy = (e.target as HTMLSelectElement).value as SearchBy;
        // set default values when searching type is changed
        this.setState(prevState => ({...prevState, filterBy, dateFrom: "", dateTo: "",
            reportResult:REPORT_RESULTS[0], reportStatus: REPORT_STATUSES[0], formError: "" }));
    };

    private onReportResultChanged = (e: FormEvent<FormControl>): void => {
        const reportResult = (e.target as HTMLSelectElement).value;
        this.setState(prevState => ({...prevState, reportResult}));
    };

    private onReportStatusChanged = (e: FormEvent<FormControl>): void => {
        const reportStatus = (e.target as HTMLSelectElement).value;
        this.setState(prevState => ({...prevState, reportStatus}));
    };

    private updateDateFrom = (date: Date | null): void => {
        if (date !== null) {
            this.setState(prevState => ({...prevState, formError: "",
                dateFrom: moment(date).format('YYYY-MM-DD')}));
        }
    };

    private updateDateTo = (date: Date | null): void => {
        if (date !== null) {
            this.setState(prevState => ({...prevState, formError: "",
                dateTo: moment(date).format('YYYY-MM-DD')}));
        }
    };

    private onSubmit = async (): Promise<void> => {
        console.table(this.state);
        let result: IReport[] | string = [];

        switch (this.state.filterBy) {
            case SEARCH_BY_EMPTY:
                result = await reportService.getAllReports();
                break;
            case SEARCH_BY_STATUS:
                result = await reportService.getReportsWithStatus(this.state.reportStatus);
                break;
            case SEARCH_BY_RESULT:
                result = await reportService.getReportsWithResult(this.state.reportResult);
                break;
            case SEARCH_BY_INTERVAL:
                const validationResult = this.validateDates();
                if (validationResult !== "") {
                    this.setState(prevState => ({...prevState, formError: validationResult}));
                    return;
                }
                result = await reportService.getReportsFromInterval(this.state.dateFrom, this.state.dateTo);
                break;
        }

        this.props.onSearch(result);
    };

    private validateDates(): string {
        if(this.state.dateFrom == null || this.state.dateFrom === "" &&
            this.state.dateTo == null || this.state.dateTo === "") {
            return "You must fill both dates";
        }
        const dateFrom = moment(this.state.dateFrom);
        const dateTo = moment(this.state.dateTo);
        if (dateFrom.isAfter(dateTo)) {
            return "Date from cannot have higher value than date to";
        }

        return "";
    }

    /********************************************************
     * LIFE-CYCLE METHODS
     *******************************************************/
    constructor(props: IProps) {
        super(props);

        this.state = {
            filterBy: SEARCH_BY_EMPTY,
            reportStatus: REPORT_STATUSES[0],
            reportResult: REPORT_RESULTS[0],
            dateFrom: "",
            dateTo: "",
            formError: "",
        }
    }

    /********************************************************
     * RENDERING
     *******************************************************/
    private getSearchByOptions(): JSX.Element[] {
        return filters.map((filter, index) => (
            <option key={index} value={filter}>{filter}</option>
        ));
    }

    private getValuesBasedOnFilter(): JSX.Element | null {
        switch (this.state.filterBy) {
            case SEARCH_BY_EMPTY:
                return null;
            case SEARCH_BY_RESULT:
                return this.createReportResultSelect();
            case SEARCH_BY_STATUS:
                return this.createReportStatusSelect();
            case SEARCH_BY_INTERVAL:
                return this.createDatePickers();
            default:
                return null;
        }
    }

    private createReportResultSelect(): JSX.Element {
        const options = REPORT_RESULTS.map((result, index) => (
            <option value={result} key={index} >{result}</option>
        ));

        return this.createSelect(options, "Select result:", "reportResult", this.onReportResultChanged);
    }

    private createReportStatusSelect(): JSX.Element {
        const options = REPORT_STATUSES.map((status, index) => (
            <option value={status} key={index} >{status}</option>
        ));

        return this.createSelect(options, "Select status:", "reportStatus", this.onReportStatusChanged);
    }

    private createSelect(options: JSX.Element[], selectTitle: string, stateProp: string,
                         changeCallback: (e: FormEvent<FormControl>) => void): JSX.Element {
        return (
            <FormGroup>
                <InputGroup className={"ReportFilter__inputGroup"}>
                    <InputGroup.Addon><span className={"input-group-text"}>{selectTitle}</span></InputGroup.Addon>
                    <FormControl componentClass={'select'}
                                 value={this.state[stateProp]}
                                 onChange={changeCallback}>
                        {options}
                    </FormControl>
                </InputGroup>
            </FormGroup>
        );
    }

    private createDatePickers(): JSX.Element {
        return (
            <div className={"ReportFilter__dates"}>
                <DatePicker value={this.state.dateFrom} onChange={this.updateDateFrom}/>
                <DatePicker value={this.state.dateTo} onChange={this.updateDateTo}/>
            </div>
        )
    }

    public render(): JSX.Element {
        return (
            <div className={"ReportFilter"}>
                <form className={"ReportFilter__form"}>
                    <FormGroup>
                        <InputGroup className={"ReportFilter__inputGroup"}>
                            <InputGroup.Addon><span className={"input-group-text"}>Filter by:</span></InputGroup.Addon>
                            <FormControl componentClass={'select'}
                                         value={this.state.filterBy}
                                         onChange={this.onSearchByChanged}>
                                {this.getSearchByOptions()}
                            </FormControl>
                        </InputGroup>
                    </FormGroup>

                    {this.state.filterBy !== SEARCH_BY_EMPTY && (
                        this.getValuesBasedOnFilter()
                    )}
                </form>
                <Button  onClick={this.onSubmit}>Submit</Button>

                {this.state.formError !== "" && (
                    <Alert bsStyle={"danger"} className={"ReportFilter__alert"}>{this.state.formError}</Alert>
                )}
            </div>
        )
    }

}