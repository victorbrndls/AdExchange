import {HOST} from "./configs";
import Axios from 'axios';
import Logger from './components/utils/Logger';

const storage = window.localStorage;

let auth = {
    getToken,
    isUserAuthenticated
};

function login(email, password) {
    let formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);

    return new Promise((resolve, reject) => {
        AdAxiosPost.post(`${HOST}/auth/login`, formData).then((response) => {
            saveToken(response.data);
            reloadAxiosInstances();

            resolve();
        }).catch((error) => {
            reject(error.response.data);
        });
    })
}

function createAccount(email, password) {
    let formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);

    return new Promise((resolve, reject) => {
        AdAxiosPost.post(`${HOST}/auth/account`, formData).then(() => {
            //TODO login user after account is created
            resolve();
        }).catch((error) => {
            reject(error.response.data);
        });
    });
}

function logout() {
    storage.removeItem('adExchange.authToken');
    reloadAxiosInstances();
}

function saveToken(token) {
    if (token !== undefined)
        storage.setItem('adExchange.authToken', token);
}

function getToken() {
    return storage.getItem('adExchange.authToken');
}

function isUserAuthenticated() {
    return getToken() !== null;
}

/**
 * When the instances are created they have the token value, if the user signs in or signs out the instances don't
 * update the token value by themselves. This method recreated the instances with the new token value
 */
function reloadAxiosInstances() {
    Logger.log("Reload Axios Instances");
    AdAxiosPost = createAxiosPostInstance();
    AdAxiosGet = createAxiosGetInstance();
}

let AdAxiosPost = createAxiosPostInstance();
let AdAxiosGet = createAxiosGetInstance();

function createAxiosPostInstance() {
    return Axios.create({
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Token ${getToken()}`
        }
    });
}

function createAxiosGetInstance() {
    return Axios.create({
        headers: {
            Authorization: `Token ${getToken()}`
        }
    });
}

export {
    auth,
    login,
    logout,
    createAccount,
    AdAxiosPost,
    AdAxiosGet
}