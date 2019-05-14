/**
 * @return {Promise} The notifications that belong to the user as an array
 */
import {AdAxiosGet} from "../auth";
import {HOST} from "../configs";

function getNotifications() {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/notifications/me`).then((response) => {
            resolve(response.data);
        });
    });
}

export default {
    getNotifications
};