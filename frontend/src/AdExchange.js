// Script to add the Ads in the website
(function () {
    const HOST = 'https://localhost:8080';

    if (document.readyState === 'complete') {
        init();
    } else {
        window.addEventListener('load', () => {
            init();
        }, false);
    }

    function init() {
        let adPlaceHolders = Array.from(document.querySelectorAll('[data-ae-id]'));
        let ids = '';

        adPlaceHolders.forEach((ad) => ids += ad.getAttribute('data-ae-id') + ',');

        requestAds(ids, (ads) => {
            ads.forEach((adModel) => {
                if (adModel.error !== null) {
                    // TODO handle error response
                }

                let container = document.querySelector(`[data-ae-id="${adModel.spotId}"]`);
                container.innerHTML = adModel.content;
            });

        });
    }

    function requestAds(ids, cb) {
        let xml = new XMLHttpRequest();

        xml.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                let response = JSON.parse(this.responseText);

                if (response === undefined)
                    return;

                cb(response);
            }
        };
        xml.open("GET", `${HOST}/serve/v1/spots?ids=${ids}`, true);
        xml.send();
    }

})();

