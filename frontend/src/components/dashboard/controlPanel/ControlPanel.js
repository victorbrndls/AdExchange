import {Component} from "preact";
import AnalyticsManager from "../../../managers/AnalyticsManager";
import ContractManager from "../../../managers/ContractManager";
import DashboardChartContainer from "./DashboardLineChart";
import DashboardPieChart from "./DashboardPieChart";

export default class ControlPanel extends Component {
    constructor(props) {
        super(props);

        this.state = {
            contracts: [],
            chartData: {
                view: {},
                click: {}
            }
        };

        this.chartClasses = {
            views: {
                name: "Visualizações",
                uniqueName: "Visualizações Únicas"
            },
            clicks: {
                name: "Cliques",
                uniqueName: "Cliques Únicos"
            }
        };

        this.hasRequestedContracts = false;
    }

    requestContracts() {
        if (!this.hasRequestedContracts) {
            this.hasRequestedContracts = true;

            ContractManager.getContracts().then((data) => {
                this.setState({contracts: data});
            })
        }
    }

    handleContractChange(value) {
        if (value && value !== '-1')
            this.getChartsData(value);
    }

    getChartsData(modelId) {
        AnalyticsManager.getAnalytics(modelId).then((data) => {
            if (data.length === 0) {
                this.setState({chartData: {view: {}, click: {}}});
                return;
            }

            let models = data.map((model) => {
                // Remove useless data from object
                return this.createAnalyticModel(model.date, model.totalClicks, model.uniqueClicks, model.totalViews, model.uniqueViews);
            }).sort((a, b) => {
                return new Date(a) < new Date(b); // Sort in chronological order
            });

            let filledModels = this.fillMissingDays(models);

            this.setState({
                chartData: {
                    click: {
                        date: filledModels.map((model) => this.convertDateToString(model.date)),
                        total: filledModels.map((model) => model.totalClicks),
                        unique: filledModels.map((model) => model.uniqueClicks)
                    },
                    view: {
                        date: filledModels.map((model) => this.convertDateToString(model.date)),
                        total: filledModels.map((model) => model.totalViews),
                        unique: filledModels.map((model) => model.uniqueViews)
                    }
                }
            });
        });
    }

    /**
     * When the server returns the analytic models, it doesn't return the model for days that haven't had any activity.
     * For example if nobody viewed or clicked a contract on a certain day, there is no analytic model for that day. This
     * method fills the missing days with an empty analytic model.
     *
     * @param array {Array} a sorted array of analytic models in chronological order
     **/
    fillMissingDays(array) {
        const DAY_MILLIS = 1000 * 60 * 60 * 24;

        let filledArray = [];

        for (let i = 0; i < array.length; i++) {
            if (i === array.length - 1) { // Last day
                filledArray.push(array[i]);

                continue;
            }

            let next = array[i + 1];
            let current = array[i];
            let currentDate = new Date(current.date);

            let dayDiff = (new Date(next.date) - currentDate) / DAY_MILLIS - 1;

            filledArray.push(current);

            // If there are 1 or more days between the two dates, add the missing ones
            if (dayDiff > 0) {
                for (let x = 0; x < dayDiff; x++) {
                    let missingDate = new Date(currentDate - -(DAY_MILLIS * (x + 1))); // the '+' sign doesn't work
                    filledArray.push(this.createAnalyticModel(`${missingDate.getUTCFullYear()}-${missingDate.getUTCMonth() + 1}-${missingDate.getUTCDate()}`, 0, 0, 0, 0));
                }
            }
        }

        return filledArray;
    }

    createAnalyticModel(date, tClicks, uClicks, tViews, uViews) {
        return {
            date: date,
            totalClicks: tClicks,
            uniqueClicks: uClicks,
            totalViews: tViews,
            uniqueViews: uViews
        };
    }

    /**
     * Converts a string date to 'month day' format. Eg: "2019-05-31" -> "May 31"
     *
     * @param strDate {String} a valid string that can be converted to Date
     * **/
    convertDateToString(strDate) {
        let date = new Date(strDate);
        let options = {month: 'short', day: 'numeric', timeZone: 'UTC'};

        return date.toLocaleDateString('default', options);
    }

    getClickToViewRatioData() {
        const reducer = (a, c) => a + c;

        let chartData = this.state.chartData;

        if (!chartData.click.total || !chartData.view.total)
            return {
                labels: [],
                data: []
            };

        return {
            labels: ["Cliques", "Visualizações"],
            data: [chartData.click.total.reduce(reducer), chartData.view.total.reduce(reducer)]
        }
    }

    render({}, {chartData, contracts}) {
        let chartColumnClass = "col-xl-5 col-md-6";

        return (
            <div>
                <div>
                    {this.requestContracts.bind(this)()}
                    <div style="max-width: 300px;" class="mb-2">
                        <select class='custom-select' onChange={e => this.handleContractChange(e.target.value)}>
                            <option value="-1">Selecione um contrato</option>
                            {contracts.map((contract) => (
                                <option
                                    value={contract.id}>{(contract.acceptorContractName ? contract.acceptorContractName : contract.creatorContractName) || "*Contrato sem nome*"}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div class="row">
                        <div class={chartColumnClass}>
                            <DashboardChartContainer config={this.chartClasses.clicks} data={chartData.click}/>
                        </div>
                        <div class={chartColumnClass}>
                            <DashboardChartContainer config={this.chartClasses.views} data={chartData.view}/>
                        </div>
                    </div>
                    <div class="row">
                        <div class={chartColumnClass}>
                            <DashboardPieChart data={this.getClickToViewRatioData()}/>
                        </div>
                    </div>
                </div>

                {/*Remove this latter*/}
                <div style="position: absolute;bottom: 15px;right: 22px;">
                    Imagens, gráficos, vídeos, listas, links, gifs, infográficos
                </div>
            </div>
        )
    }
}