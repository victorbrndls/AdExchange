import {AdAxiosGet} from "../auth";
import {HOST} from "../configs";

function getMyAccount() {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/account/me`).then((response) => {
            resolve(response.data);
        });
    });
}

export default {
    getMyAccount
};