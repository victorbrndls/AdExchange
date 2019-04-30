import {Component} from "preact";
import CreateAdd, {ImageAd, TextAd} from "./CreateAdd";
import {ConfirmationModal, LeftArrow} from "../../utils/Components";
import Match from "../../utils/Match";
import {route} from "preact-router";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
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

    reload() {
        this.requestAds();
    }

    deleteAd(id) {
        ConfirmationModal.renderFullScreen("", () => {
            AdAxiosPost.delete(`${HOST}/api/v1/ads/${id}`).then((response) => {
                this.requestAds();
            });
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
                            <div style="display: flex; flex-direction: column;">
                                {ads.map((ad) => (
                                    <div class="ads-ad__container shadow">
                                        <div class="ads-ad__container-name">
                                            <div class="ad__container-header">
                                                <span>{ad.name}</span>
                                            </div>
                                            <div style="margin-left: 7px;">
                                                <span class="ad__container-option">Editar</span>
                                                <span class="ad__container-option"
                                                      onClick={this.deleteAd.bind(this, ad.id)}>Deletar</span>
                                            </div>
                                        </div>
                                        <div style="margin: 12px; box-shadow: 0 0 11px -2px #0000004d;"
                                             class="ads-ad-wrapper">
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
                        <CreateAdd reload={this.reload.bind(this)}/>
                    </Match>
                </div>
            </div>
        )
    }
}