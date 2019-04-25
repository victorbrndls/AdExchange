import {Component} from "preact";
import {Website} from "../websites/Websites";
import Axios from "axios";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost, auth} from "../../../auth";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import {route} from "preact-router";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
            mode: "EDIT", // EDIT or NEW
            proposal: null,
            website: {},
            ads: [],
            selectedAd: null
        };

        this.fields = {
            duration: () => document.getElementById("p_duration"),
            paymentMethod: () => document.getElementById("p_paymentMethod"),
            paymentValue: () => document.getElementById("p_paymentValue")
        };

        this.moneyPattern = "(\\d+\\.\\d{1,2})$";

        this.updateMode();

        this.requestProposalInformation().then(() => {
            this.requestWebsiteInformation();
            this.requestAdsInformation();
        });
    }

    updateMode() {
        if (location.pathname.includes("/edit/new"))
            this.setState({mode: "NEW"});
    }

    async requestProposalInformation() {
        let proposalId = this.state.mode === 'EDIT' ? location.pathname.split("edit/")[1] : -1;

        if (proposalId === -1) {
            this.setState({
                proposal: {
                    id: "-1",
                    websiteId: new URLSearchParams(location.search).get('websiteId') || "-1"
                }
            });
            return;
        }

        let req = await AdAxiosGet.get(`${HOST}/api/v1/proposals/${proposalId}`);
    }

    /**
     * Requests information of the website that is this proposal is for
     */
    requestWebsiteInformation() {
        let id = this.state.proposal.websiteId;

        if (id === "-1") {
            this.setState({error: "websiteId invalido"});
            return;
        }

        AdAxiosGet.get(`${HOST}/api/v1/websites/${id}`).then((response) => {
            this.setState({website: response.data});
        });
    }

    /**
     * Requests all ads that belong to the user
     */
    requestAdsInformation() {
        AdAxiosGet.get(`${HOST}/api/v1/ads/me`).then((response) => {
            this.setState({ads: response.data});
        });
    }

    handleAdChange(e) {
        let id = e.target.value;

        if (id === "-1")
            return;

        this.adId = id;
        AdAxiosGet.get(`${HOST}/api/v1/ads/${id}`).then((response) => {
            this.setState({selectedAd: response.data});
        });
    }

    submitProposal() {
        let formData = new FormData();
        formData.append("websiteId", this.state.proposal.websiteId);
        formData.append("adId", this.adId);
        formData.append("duration", this.fields.duration().value);
        formData.append("paymentMethod", this.fields.paymentMethod().value);
        formData.append("paymentValue", this.fields.paymentValue().value);

        AdAxiosPost.post(`${HOST}/api/v1/proposals`, formData).then((response) => {
            route('/dashboard/proposals');
        }).catch((error) => {
            console.log(error.response.data.error);
        });
    }

    render({}, {proposal, website, proposalId, error, ads, selectedAd}) {
        if (proposal === null)
            return;

        return (
            <div>
                <div style="font-family: Raleway; font-size: 30px;">
                    Proposta para "{website.name}"
                </div>
                <div>
                    <div style="position: relative;">
                        <div class="blocking-container"/>
                        <Website {...website}/>
                    </div>

                    <div>
                        <div class="form-group websites-add__form">
                            <label>Anúncio</label>
                            <select class="custom-select" onChange={this.handleAdChange.bind(this)}>
                                <option value="-1">Selecione um anuncio</option>
                                {ads && ads.map((ad) => (
                                    <option value={ad.id}>{ad.name}</option>
                                ))}
                            </select>
                            <div style="justify-content: center; display: flex; position: relative;">
                                <div class="blocking-container"/>
                                {selectedAd && (
                                    <div class="ads-ad-wrapper mt-4">
                                        {selectedAd.type === 'TEXT' ? (<TextAd {...selectedAd}/>) : (
                                            <ImageAd {...selectedAd}/>)}
                                    </div>
                                )}
                            </div>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Duracao (dias)</label>
                            <input id="p_duration" class="form-control" placeholder="15"/>
                            <small class="form-text text-muted">Por quanto tempo o anuncio ficara ativo (de 0 a 365)</small>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Pagamento</label>
                            <select id="p_paymentMethod" class="custom-select">
                                <option value="PAY_PER_CLICK">Custo por Click</option>
                                <option value="PAY_PER_VIEW">Custo por Visualizacao</option>
                            </select>
                            <div class="mb-2"/>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">R$</span>
                                </div>
                                <input id="p_paymentValue" class="form-control" pattern={this.moneyPattern}
                                       placeholder="Valores com no maximo 2 casas decimais (1.50, 4.54, 0.10, 18.01, 0.50)"/>
                            </div>
                        </div>
                    </div>

                    <div class="btn dashboard-add__button" onClick={this.submitProposal.bind(this)}>
                        Enviar Proposta
                    </div>
                </div>
            </div>
        )
    }
}