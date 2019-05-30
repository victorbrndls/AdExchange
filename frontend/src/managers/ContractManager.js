import {AdAxiosGet} from "../auth";
import {HOST} from "../configs";

function getContract(id) {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/contracts/${id}`).then((response) => {
            resolve(response.data);
        });
    });
}

function getContracts() {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/contracts/me`).then((response) => {
            resolve(response.data);
        });
    });
}

export default {
    getContract,
    getContracts
};