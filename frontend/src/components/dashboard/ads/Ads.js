import {Component} from "preact";
import CreateAdd, {ImageAd, TextAd} from "./CreateAdd";
import {LeftArrow} from "../../utils/Components";
import Match from "../../utils/Match";
import {route} from "preact-router";
import {AdAxiosGet} from "../../../auth";
import {HOST} from "../../../configs";

export default class Ads extends Component {
    constructor(props) {
        super(props);

        this.state = {
            ads: []
        }
    }

    componentDidMount() {
        this.requestAds();
    }

    requestAds() {
        AdAxiosGet.get(`${HOST}/api/v1/ads/me`).then((response) => {
            this.setState(
                {ads: response.data}
            );
        });
    }

    render({}, {ads}) {
        return (
            <div>
                <Match path={"/dashboard/ads"} not>
                    <LeftArrow cb={() => route('/dashboard/ads')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/ads" exact>
                        <div>
                            <div class="ads-add dashboard-website__rounded-button" style="margin-bottom: 15px;"
                                 onClick={() => route('/dashboard/ads/create')}>
                                Criar Anúncio
                            </div>
                            <div style="display: flex; flex-wrap: wrap;">
                                {ads.map((ad) => (
                                    <div class="ads-ad__container">
                                        <div class="ads-ad__container-name">
                                            <div class="ad__container-header">
                                                <h5>{ad.name}</h5>
                                            </div>
                                            <div style="margin-left: 7px;">
                                                <span class="ad__container-option">Editar</span>
                                                <span class="ad__container-option">Deletar</span>
                                            </div>
                                        </div>
                                        <div style="margin: 12px; box-shadow: 0 0 11px -2px #0000004d;" class="ads-ad-wrapper">
                                            {ad.type === 'TEXT' ?
                                                (<TextAd {...ad}/>) :
                                                (<ImageAd {...ad}/>)}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </Match>

                    <Match path="/dashboard/ads/create" exact>
                        <CreateAdd/>
                    </Match>
                </div>
            </div>
        )
    }
}