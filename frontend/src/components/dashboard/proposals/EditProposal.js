import {Component} from "preact";
import {Website} from "../websites/Websites";
import Axios from "axios";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost, auth} from "../../../auth";
import {ImageAd, TextAd} from "../ads/CreateAdd";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
            mode: "EDIT", // EDIT or NEW
            proposalId: -1,
            website: {},
            ads: [],
            selectedAd: null
        };

        this.moneyPattern = "(\\d+\\.\\d{1,2})$";

        this.updateMode();

        this.setState({proposalId: this.getProposalId()});
        this.requestWebsiteInformation();
        this.requestAdsInformation();
    }

    updateMode() {
        if (location.pathname.includes("/edit/new"))
            this.setState({mode: "NEW"});
    }

    getWebsiteId() {
        switch (this.state.mode) {
            case "EDIT":
                return 0;
            case "NEW":
                let query = new URLSearchParams(location.search);
                return query.get('websiteId') || -1;
            default:
                return -1;
        }
    }

    getProposalId(){
        switch (this.state.mode) {
            case "EDIT":
                return location.pathname.split("edit/")[1] || -1;
            case "NEW":
            default:
                return -1;
        }
    }

    /**
     * Requests information of the website that is this proposal is for
     */
    requestWebsiteInformation() {
        let id = this.getWebsiteId();

        if (id === -1) {
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
        if (e.target.value === "-1")
            return;

        AdAxiosGet.get(`${HOST}/api/v1/ads/${e.target.value}`).then((response) => {
            this.setState({selectedAd: response.data});
        });
    }

    submitProposal() {
        let formData = new FormData();
        formData.append("websiteId", this.getWebsiteId());

        AdAxiosPost.post(`${HOST}/api/v1/proposals`, formData).then((response) => {

        }).catch((error)=>{
            console.log(error);
        });
    }

    render({}, {website, proposalId, error, ads, selectedAd}) {
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
                            <small class="form-text text-muted">Por quanto tempo o anuncio ficara ativo</small>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Pagamento</label>
                            <select class="custom-select">
                                <option>Custo por Click</option>
                                <option>Custo por Visualizacao</option>
                            </select>
                            <div class="mb-2"/>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">R$</span>
                                </div>
                                <input class="form-control" pattern={this.moneyPattern}
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