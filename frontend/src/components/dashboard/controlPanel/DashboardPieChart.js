import {Component} from "preact";
import LazyLoading from "../../utils/LazyLoading";

export default class DashboardPieChart extends Component {
    static ID = 1;

    constructor(props) {
        super(props);

        this.data = {
            labels: [],
            data: []
        };

        this.id = `dpc${DashboardPieChart.ID++}`;
    }

    componentWillReceiveProps(props /*nextProps*/) {
        if (!props.data)
            return;

        this.data = {
            labels: props.data.labels ? props.data.labels : [],
            data: props.data.data ? props.data.data : []
        };

        this.renderChart();
    }

    renderChart() {
        LazyLoading.getChartJS().then((Chart) => {
            if (this.chart) {
                let chartData = this.chart.data;
                let data = this.data;

                chartData.datasets[0].data = data.data;
                chartData.labels = data.labels;

                this.chart.update();
            } else {
                this.chart = new Chart(document.getElementById(this.id), {
                    type: 'doughnut',
                    data: {
                        datasets: [
                            {
                                data: this.data.data,
                                backgroundColor: ["#7835f4", "#b1f435"]
                            }],
                        labels: this.data.labels
                    },
                    options: {}
                });
            }
        });
    }

    render() {
        return (
            <div class="card mb-4">
                <div>
                    <canvas id={this.id}/>
                </div>
            </div>
        )
    }
}