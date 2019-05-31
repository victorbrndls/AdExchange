import {Component} from "preact";
import LazyLoading from "../../utils/LazyLoading";

export default class DashboardChartContainer extends Component {
    static ID = 1;

    constructor(props) {
        super(props);

        this.state = {
            name: this.props.config.name,
            uniqueName: this.props.config.uniqueName
        };

        this.data = {
            total: [0],
            unique: [0]
        };

        this.id = `dlc${DashboardChartContainer.ID++}`;
    }

    componentWillReceiveProps(props /*nextProps*/) {
        if (!props.data)
            return;

        this.data = {
            date: props.data.date ? props.data.date : [],
            total: props.data.total ? props.data.total : [0],
            unique: props.data.unique ? props.data.unique : [0]
        };

        this.renderChart();
    }

    renderChart() {
        LazyLoading.getChartJS().then((Chart)=>{
            if (this.chart) {
                let chartData = this.chart.data;
                let data = this.data;

                chartData.labels = data.date;
                chartData.datasets[0].data = data.total;
                chartData.datasets[1].data = data.unique;

                this.chart.update();
            } else {
                this.chart = new Chart(document.getElementById(this.id), {
                    type: 'line',
                    data: {
                        labels: this.data.date,
                        datasets: [
                            {
                                label: this.state.name,
                                data: this.data.total,
                                backgroundColor: "#1689cf",
                                borderColor: "#1689cf",
                                fill: false
                            },
                            {
                                label: this.state.uniqueName,
                                data: this.data.unique,
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
        });
    }

    componentWillUnmount(){
        if(this.chart)
            this.chart.destroy();
    }

    render({}, {name, uniqueName}) {
        const reducer = (a, c) => a + c;

        let total = this.data.total;
        let unique = this.data.unique;

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
                <div class="to-remove">
                    <canvas id={this.id}/>
                </div>
            </div>
        )
    }
}