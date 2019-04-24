import {HOST} from "./configs";
import Axios from 'axios';

const storage = window.localStorage;

let auth = {
    getToken: () => getToken(),
    isUserAuthenticated: () => getToken() !== null
};


function login(email, password) {
    let formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);

    return new Promise((resolve, reject) => {
        Axios.post(`${HOST}/auth/login`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then((response) => {
            saveToken(response.data.token);
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
        Axios.post(`${HOST}/auth/account`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then((response) => {
            //TODO login user after account is created
            resolve();
        }).catch((error) => {
            reject(error.response.data);
        });
    });
}

function logout() {
    storage.removeItem('adExchange.authToken');
}

function saveToken(token) {
    if(token !== undefined)
        storage.setItem('adExchange.authToken', token);
}

function getToken() {
    return storage.getItem('adExchange.authToken');
}

const AdAxiosPost = Axios.create({
    headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Token ${getToken()}`
    }
});

const AdAxiosGet = Axios.create({
    headers: {
        Authorization: `Token ${getToken()}`
    }
});

export {
    auth,
    login,
    logout,
    createAccount,
    AdAxiosPost,
    AdAxiosGet
}