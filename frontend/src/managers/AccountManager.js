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

function saveAccountAuth(email, password) {
    return new Promise((resolve, reject) => {
        let formData = new FormData();
        formData.append("email", email);
        formData.append("password", password);

        AdAxiosPost.patch(`${HOST}/api/v1/account?form=auth`, formData).then((response) => {
            resolve(response.data);
        }).catch((error) => {
            reject(error.response.data);
        });
    });
}

function requestBalance() {
    return new Promise((resolve, reject) => {
        AdAxiosPost.get(`${HOST}/api/v1/account?fields=balance`).then((response) => {
            resolve(response.data);
        }).catch((error) => {
            reject(error.response.data);
        });
    });
}

function requestMoneyWithdraw(mode, value, params) {
    let formData = new FormData();
    formData.append("mode", mode);
    formData.append("value", value);

    Object.entries(params).forEach((entry) => formData.append(entry[0], entry[1]));

    return new Promise((resolve, reject) => {
        AdAxiosPost.post(`${HOST}/api/v1/account/withdraw-balance`, formData).then((response) => {
            resolve(response.data);
        }).catch((error) => {
            reject(error.response.data);
        });
    });
}

export default {
    getMyAccount,
    saveAccountInfo,
    saveAccountAuth,
    requestBalance,
    requestMoneyWithdraw
};