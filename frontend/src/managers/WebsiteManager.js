import {AdAxiosGet} from "../auth";
import {HOST} from "../configs";

function getWebsites() {
    return getWebsiteWithCategories([]);
}

/**
 *
 * @param categories {[string]}
 */
function getWebsiteWithCategories(categories) {
    return new Promise((resolve) => {
        AdAxiosGet.get(`${HOST}/api/v1/websites?categories=${categories}`).then((response) => {
            resolve(response.data);
        });
    });
}

export default {
    getWebsites,
    getWebsiteWithCategories
};