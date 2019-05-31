const chartJS = undefined;

function getChartJS() {
    return new Promise((resolve, reject) => {
        if (chartJS) {
            resolve(chartJS);
        } else {
            resolve(import('chart.js'));
        }
    });
}

export default {
    getChartJS
}