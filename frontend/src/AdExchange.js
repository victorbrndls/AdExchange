// Script to load the Ads in the website
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

        requestAds(ids, createAdsInPage);

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

    function createAdsInPage(adModels) {
        adModels.forEach((adModel) => {
            if (adModel.error !== null) {
                // TODO handle error response
                return;
            }

            let container = document.querySelector(`[data-ae-id="${adModel.spotId}"]`);

            if (container === null)
                return; // TODO handle null container

            let template = document.createElement('template');
            template.innerHTML = adModel.content;

            addRefUrlToATag(template.content.firstChild, adModel.redirectUrl);

            // Escape text content but not image

            container.parentNode.insertBefore(template.content.firstChild, container);
        });
    }

    /**
     * Sets the 'href' attribute in the <a>
     * @param tag
     * @param url
     */
    function addRefUrlToATag(tag, url) {
        if (tag.nodeName === 'A') {
            tag.setAttribute('href', url);
        }
    }

    // https://github.com/janl/mustache.js/blob/master/mustache.js#L73
    let entityMap = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
        '/': '&#x2F;',
        '`': '&#x60;',
        '=': '&#x3D;'
    };

    function escapeHtml(string) {
        return String(string).replace(/[&<>"'`=\/]/g, function fromEntityMap(s) {
            return entityMap[s];
        });
    }

})();

