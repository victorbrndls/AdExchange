import {HOST} from "./configs";
import Axios from 'axios';
const storage = window.localStorage;

let auth = {
    getToken: () => getToken()
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
            saveToken(response.data.getToken);
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

    return new Promise((resolve, reject)=>{
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

function saveToken(token) {
    storage.setItem('adExchange.authToken', token);
}

function getToken() {
    return storage.getItem('adExchange.authToken');
}

export {
    auth,
    login,
    createAccount
}