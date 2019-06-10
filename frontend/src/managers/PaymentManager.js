import {AdAxiosGet, AdAxiosPost} from "../auth";
import {HOST} from "../configs";

const BALANCE_PRODUCT = {
    BALANCE_25: 'BALANCE_25',
    BALANCE_50: 'BALANCE_50',
    BALANCE_100: 'BALANCE_100'
};

function getCheckoutCode(balanceProduct) {
    return new Promise((resolve) => {
        let formData = new FormData();
        formData.append("product", balanceProduct);

        AdAxiosPost.post(`${HOST}/api/v1/payments/checkout/`, formData).then((response) => {
            resolve(response.data);
        });
    });
}

export default {
    BALANCE_PRODUCT,
    getCheckoutCode
};