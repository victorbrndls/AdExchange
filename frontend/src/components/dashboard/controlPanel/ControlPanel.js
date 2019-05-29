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
                    uniqueName: "Visualizações Únicas",
                    labels: [],
                    totalData: [],
                    uniqueData: []
                },
                clicks: {
                    name: "Cliques",
                    uniqueName: "Cliques Únicos",
                    labels: [],
                    totalData: [],
                    uniqueData: []
                }
            }
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
                return this.createAnalyticModel(model.date, model.totalClicks, model.uniqueClicks, model.totalViews, model.uniqueViews);
            }).sort((a, b) => {
                return new Date(a) < new Date(b);
            });

            let filledModels = this.fillMissingDays(models);

            console.log(filledModels);

            this.setState({chartClasses: {...this.state.chartClasses, hasBeenLoaded: true}})
        });
    }

    fillMissingDays(array) {
        let filledArray = [];

        for (let i = 0; i < array.length; i++) {
            if(i === array.length -1){
                filledArray.push(array[i]);
                continue;
            }

            let next = array[i + 1];
            let current = array[i];

            let diff = (new Date(next.date) - new Date(current.date)) / 24 / 60 / 60 / 1000 - 1; // day

            filledArray.push(current);

            if (diff > 0) {
                for (let x = 0; x < diff; x++) {
                    filledArray.push(this.createAnalyticModel("2019-05-XX", 0, 0, 0, 0));
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
                            <DashboardChartContainer config={chartClasses.clicks}/>
                        </div>
                    )}
                    {chartClasses.hasBeenLoaded && (
                        <div class={chartColumnClass}>
                            <DashboardChartContainer config={chartClasses.views}/>
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
            uniqueName: this.props.config.uniqueName
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

            let labels = [];


            new ChartJS(document.getElementById(this.state.id), {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: this.state.name,
                            data: [5, 16, 7],
                            borderColor: "blue"
                        },
                        {
                            label: this.state.uniqueName,
                            data: [1, 3, 1],
                            borderColor: "#2dc9cc"
                        }]
                },
                options: {
                    elements: {
                        line: {
                            tension: 0
                        }
                    }
                }
            });
        }
    }

    render({}, {id, name, uniqueName}) {
        return (
            <div class="card mb-4">
                <div class="card-body d-flex justify-content-between">
                    <div class="controlpanel-card__text">
                        <h2 class="m-0">250</h2>
                        <span>{name}</span>
                    </div>
                    <div class="controlpanel-card__text text-right">
                        <h2 class="m-0">25</h2>
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