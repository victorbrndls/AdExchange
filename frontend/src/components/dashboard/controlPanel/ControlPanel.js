import {Component} from "preact";
import ControlPanelManager from "../../../managers/ControlPanelManager";
import AnalyticsManager from "../../../managers/AnalyticsManager";

export default class ControlPanel extends Component {
    constructor(props) {
        super(props);

        this.state = {
            notifications: [],
            contractId: "5cec7688f669ff0758cf68ce",
            chartClasses: {
                hasBeenLoaded: false,
                views: {
                    name: "Visualizações",
                    uniqueName: "Visualizações Únicas"
                },
                clicks: {
                    name: "Cliques",
                    uniqueName: "Cliques Únicos"
                }
            }
        };

        this.chartData = {
            view: {},
            click: {}
        };

        this.getChartsData();

        this.hasRequestedNotifications = false;
    }

    requestNotifications() {
        if (!this.hasRequestedNotifications) {
            this.hasRequestedNotifications = true;
            ControlPanelManager.getNotifications().then((notifs) => {
                this.setState({notifications: notifs.reverse()});
            });
        }
    }

    getChartsData() {
        AnalyticsManager.getAnalytics(this.state.contractId).then((data) => {
                let models = data.map((model) => {
                    // Remove useless data from object
                    return this.createAnalyticModel(model.date, model.totalClicks, model.uniqueClicks, model.totalViews, model.uniqueViews);
                }).sort((a, b) => {
                    return new Date(a) < new Date(b); // Sort in chronological order
                });

                let filledModels = this.fillMissingDays(models);

                this.chartData.click = {
                    date: filledModels.map((model) => model.date),
                    total: filledModels.map((model) => model.totalClicks),
                    unique: filledModels.map((model) => model.uniqueClicks)
                };

                this.chartData.view = {
                    date: filledModels.map((model) => model.date),
                    total: filledModels.map((model) => model.totalViews),
                    unique: filledModels.map((model) => model.uniqueViews)
                };

                this.setState({chartClasses: {...this.state.chartClasses, hasBeenLoaded: true}})
            }
        )
        ;
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
            if (i === array.length - 1) {
                filledArray.push(array[i]);
                continue;
            }

            let next = array[i + 1];
            let current = array[i];
            let currentDate = new Date(current.date);

            let diff = (new Date(next.date) - currentDate) / DAY_MILLIS - 1;

            filledArray.push(current);

            if (diff > 0) {
                for (let x = 0; x < diff; x++) {
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

    render({}, {notifications, chartClasses}) {
        let chartColumnClass = "col-xl-5 col-md-6";

        return (
            <div>
                <div class="row">
                    {chartClasses.hasBeenLoaded && (
                        <div class={chartColumnClass}>
                            <DashboardChartContainer config={chartClasses.clicks} data={this.chartData.click}/>
                        </div>
                    )}
                    {chartClasses.hasBeenLoaded && (
                        <div class={chartColumnClass}>
                            <DashboardChartContainer config={chartClasses.views} data={this.chartData.view}/>
                        </div>)
                    }
                </div>

                <div class="col-sm-12 col-md-7 col-lg-4 d-none">
                    {this.requestNotifications.bind(this)()}
                    {notifications.length > 0 && (<div class="card">
                        <div class="card-header dashboard-panel__notification-card">
                            <span class="dashboard-panel__notification-header">Notificações</span>
                        </div>
                        <div class="dashboard-panel__notifications card-body">
                            {notifications.map((notif) => (
                                <div>
                                    <NotificationItem {...notif}/>
                                </div>
                            ))}
                        </div>
                    </div>)}
                </div>

                {/*Remove this latter*/}
                <div style="position: absolute;bottom: 15px;right: 22px;">
                    <h2>Develop only the MVP and just that</h2>

                    <div>How can I develop this feature better or in a different way?<br/></div>
                    <div>Design Pattern? / Different Architecture?<br/></div>
                    <div>What is the simplest thing that could possibly work?<br/></div>
                    <br/>

                    Imagens, gráficos, vídeos, listas, links, gifs, infográficos
                </div>
            </div>
        )
    }
}

class NotificationItem extends Component {
    notificationMapper = {
        'NEW_PROPOSAL': NewProposalNotification,
        'REJECTED_PROPOSAL': RejectedProposalNotification,
        'REVIEWED_PROPOSAL': ReviewedProposalNotification,
        'ACCEPTED_PROPOSAL': AcceptedProposalNotification
    };

    constructor(props) {
        super(props);
    }

    render(props) {
        let type = props.type;
        let notification = this.notificationMapper[type];

        if (!notification)
            return;

        return (
            <div class="dashboard-panel__notification--item">
                <i class={`fa ${notification._iconClass} notification-icon`}/>
                <span>{notification._messageConvertor({...props})}</span>
            </div>
        )
    }
}

class NotificationType {
    constructor(iconClass, converter) {
        this._iconClass = iconClass;
        this._messageConvertor = converter || (() => "Erro ao mostrar notificacao");
    }
}

let NewProposalNotification = new NotificationType('fa-envelope',
    ({senderName, websiteName}) => `${senderName || 'Alguem'} enviou uma proposta para ${websiteName}`);

let RejectedProposalNotification = new NotificationType('fa-minus',
    ({senderName, websiteName}) => `${senderName || 'Alguem'} rejeitou a proposta para ${websiteName}`);

let ReviewedProposalNotification = new NotificationType('fa-repeat',
    ({senderName, websiteName}) => `${senderName || 'Alguem'} revisou a proposta para ${websiteName} e enviou-la novamente`);

let AcceptedProposalNotification = new NotificationType('fa-check',
    ({websiteName}) => `A proposta para ${websiteName} foi aceita`);

class DashboardChartContainer extends Component {
    static ID = 1;

    constructor(props) {
        super(props);

        this.state = {
            chartJs: undefined,
            id: `dashboardChart${DashboardChartContainer.ID++}`,
            name: this.props.config.name,
            uniqueName: this.props.config.uniqueName,
            date: this.props.data.date,
            total: this.props.data.total,
            unique: this.props.data.unique
        };

        this.hasChartBeenRendered = false;
    }

    componentDidMount() {
        if (!this.state.chartJs) {
            import('chart.js').then(module => this.setState({chartJs: module}));
        }
    }

    componentDidUpdate() {
        if (this.state.chartJs)
            this.renderChart();
    }

    renderChart() {
        if (!this.hasChartBeenRendered) {
            this.hasChartBeenRendered = true;

            let ChartJS = this.state.chartJs;

            let state = this.state;

            new ChartJS(document.getElementById(this.state.id), {
                type: 'line',
                data: {
                    labels: state.date,
                    datasets: [
                        {
                            label: this.state.name,
                            data: state.total,
                            backgroundColor: "#1689cf",
                            borderColor: "#1689cf",
                            fill: false
                        },
                        {
                            label: this.state.uniqueName,
                            data: state.unique,
                            backgroundColor: "#cf5c16",
                            borderColor: "#cf5c16",
                            fill: false
                        }]
                },
                options: {
                    elements: {
                        line: {
                            tension: 0
                        }
                    },
                    tooltips: {
                        intersect: false,
                        mode: 'index'
                    }
                }
            });
        }
    }

    render({}, {id, name, uniqueName, total, unique}) {
        const reducer = (a, c) => a + c;

        return (
            <div class="card mb-4">
                <div class="card-body d-flex justify-content-between">
                    <div class="controlpanel-card__text">
                        <h2 class="m-0">{total.reduce(reducer)}</h2>
                        <span>{name}</span>
                    </div>
                    <div class="controlpanel-card__text text-right">
                        <h2 class="m-0">{unique.reduce(reducer)}</h2>
                        <span>{uniqueName}</span>
                    </div>
                </div>
                <div>
                    <canvas id={id}/>
                </div>
            </div>
        )
    }
}