import {AdAxiosGet, AdAxiosPost} from "../auth";
import {HOST} from "../configs";

function getMyAccount() {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/account/me`).then((response) => {
            resolve(response.data);
        });
    });
}

function saveAccountInfo(name) {
    return new Promise((resolve, reject) => {
        let formData = new FormData();
        formData.append("name", name);

        AdAxiosPost.patch(`${HOST}/api/v1/account?form=info`, formData).then((response) => {
            resolve(response.data);
        }).catch((error) => {
            reject(error.response.data);
        });
    });

}

export default {
    getMyAccount,
    saveAccountInfo
};