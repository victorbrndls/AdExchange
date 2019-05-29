import {AdAxiosGet, AdAxiosPost} from "../auth";
import {HOST} from "../configs";

function getAnalytics(id) {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/analytics/${id}`).then((response) => {
            resolve(response.data);
        });
    });

}

export default {
    getAnalytics
};