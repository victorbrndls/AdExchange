import {AdAxiosGet, AdAxiosPost} from "../auth";
import {HOST} from "../configs";

function getNotificationsStatus() {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/notifications/me/status`).then((response) => {
            resolve(response.data);
        });
    });
}

function setNotificationsStatus({newNotifications}) {
    return new Promise((resolve, reject) => {
        let formData = new FormData();
        formData.append('notifyNewNotifications', newNotifications);

        AdAxiosPost.post(`${HOST}/api/v1/notifications/me/status`, formData).then(() => resolve()
        ).catch(() => reject());
    });
}

export default {
    getNotificationsStatus,
    setNotificationsStatus
};