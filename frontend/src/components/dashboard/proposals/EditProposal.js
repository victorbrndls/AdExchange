import {Component} from "preact";
import {Website} from "../websites/Websites";
import Axios from "axios";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost, auth} from "../../../auth";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import {route} from "preact-router";
import UrlUtils from "../../utils/UrlUtils";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            mode: "EDIT", // EDIT or NEW
            type: null, // NEW or SENT
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

        this.moneyPattern = "^(\\d+(\\.\\d{1,2}){0,1})$";

        this.updateMode();

        this.requestProposalInformation().then(() => {
            this.requestWebsiteInformation();
            this.requestAdsInformation();
        });
    }

    updateMode() {
        if (UrlUtils.include("/edit/new"))
            this.setState({mode: "NEW"});

        if (this.state.mode === 'EDIT') {
            this.setState({type: new URLSearchParams(location.search).get('type')});
        }
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

        let res = await AdAxiosGet.get(`${HOST}/api/v1/proposals/${proposalId}`);
        this.setState({proposal: res.data});
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
        if (this.state.proposal.adId !== undefined) {
            AdAxiosGet.get(`${HOST}/api/v1/ads/${this.state.proposal.adId}`).then((response) => {
                this.setState({selectedAd: response.data});
            });
        } else {
            AdAxiosGet.get(`${HOST}/api/v1/ads/me?embed=parsedOutput`).then((response) => {
                this.setState({ads: response.data});
            });
        }
    }

    handleAdChange(e) {
        let id = e.target.value;

        if (id === "-1")
            return;

        this.adId = id;

        let selectedAd = this.state.ads.filter(ad => ad.id === id)[0];

        selectedAd ? this.setState({selectedAd: selectedAd}) : undefined;
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
            this.props.reload();
        }).catch((error) => {
            this.handleErrorResponse(error);
        });
    }

    acceptProposal() {
        AdAxiosPost.post(`${HOST}/api/v1/proposals/accept/${this.state.proposal.id}`).then((response) => {
            route('/dashboard/contracts');
        });
    }

    sendProposalRevision() {
        let formData = new FormData();
        formData.append("duration", this.fields.duration().value);
        formData.append("paymentMethod", this.fields.paymentMethod().value);
        formData.append("paymentValue", this.fields.paymentValue().value);

        AdAxiosPost.post(`${HOST}/api/v1/proposals/revision/${this.state.proposal.id}`, formData).then((response) => {
            route('/dashboard/proposals');
            this.props.reload();
        }).catch((error) => {
            this.handleErrorResponse(error);
        });
    }

    rejectProposal() {
        AdAxiosPost.post(`${HOST}/api/v1/proposals/reject/${this.state.proposal.id}`).then((response) => {
            route('/dashboard/proposals');
            this.props.reload();
        });
    }

    deleteProposal() {
        AdAxiosPost.delete(`${HOST}/api/v1/proposals/${this.state.proposal.id}`).then((response) => {
            route('/dashboard/proposals');
            this.props.reload();
        }).catch((error) => {
            console.log(error.response);
        });
    }

    handleErrorResponse(errorResponse) {
        this.setState({error: {}});

        let error = this.state.error;

        switch (errorResponse.response.data) {
            case 'INVALID_WEBSITE_ID':
                this.setState({error: {...error, website: "ID do website inválido"}});
                return;
            case 'INVALID_AD_ID':
                this.setState({error: {...error, ad: "ID to anúncio inválido, tente outro"}});
                return;
            case 'INVALID_DURATION':
                this.setState({
                    error: {
                        ...error,
                        duration: "Duração inválida. A duração deve ser um número entre 1 e 365"
                    }
                });
                return;
            case 'INVALID_PAYMENT_VALUE':
                this.setState({
                    error: {
                        ...error,
                        paymentValue: "Valor do pagamento inválido. O valor deve ser maior que 0.00 e conter no máximo 2 casas decimais"
                    }
                });
                return;
            case 'INVALID_PAYMENT_METHOD':
                this.setState({error: {...error, paymentMethod: "Método de pagamento inválido"}});
                return;
        }
    }

    render({}, {proposal, website, error, ads, selectedAd, mode, type}) {
        if (proposal === null)
            return;

        let edit_m = mode === 'EDIT';
        let new_m = mode === 'NEW';
        let sent_t = type === 'SENT';
        let new_t = type === 'NEW';

        let disableFields = sent_t || proposal.rejected;

        return (
            <div>
                <div style="font-family: Raleway; font-size: 30px;">
                    Proposta para "{website.name}"
                    {proposal.rejected && (<span class="badge badge-danger ml-3">Rejeitada</span>)}
                </div>
                <div>
                    <div style="position: relative;">
                        <div class="blocking-container"/>
                        <Website {...website}/>
                        <small class="form-text ad-error">{error.website}</small>
                    </div>

                    <div>
                        <div class="form-group websites-add__form">
                            <label>Anúncio</label>
                            <select class="custom-select" onChange={this.handleAdChange.bind(this)}
                                    disabled={edit_m}>
                                <option value="-1">Selecione um anuncio</option>
                                {ads && ads.map((ad) => (
                                    <option value={ad.id}>{ad.name}</option>
                                ))}
                            </select>
                            <small class="form-text ad-error">{error.ad}</small>
                            <div class="ad-container">
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
                            <input id="p_duration" class="form-control" placeholder="15"
                                   value={proposal.duration || ""} disabled={disableFields}
                                   onChange={(e) => this.setState({
                                       proposal: {
                                           ...proposal,
                                           duration: e.target.value
                                       }
                                   })}/>
                            <small class="form-text ad-error">{error.duration}</small>
                            <small class="form-text text-muted">Por quanto tempo o anuncio ficara ativo (de 1 a 365)
                            </small>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Pagamento</label>
                            <select id="p_paymentMethod" class="custom-select"
                                    value={proposal.paymentMethod || "PAY_PER_CLICK"}
                                    disabled={disableFields}
                                    onChange={(e) => this.setState({
                                        proposal: {
                                            ...proposal,
                                            paymentMethod: e.target.value
                                        }
                                    })}>
                                <option value="PAY_PER_CLICK">Custo por Click</option>
                                <option value="PAY_PER_VIEW">Custo por Visualização</option>
                            </select>
                            <small class="form-text ad-error">{error.paymentMethod}</small>
                            <div class="mb-2"/>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">R$</span>
                                </div>
                                <input id="p_paymentValue" class="form-control" pattern={this.moneyPattern}
                                       placeholder="Valores com no maximo 2 casas decimais (1.50, 4.54, 0.10, 18.01, 0.50)"
                                       value={proposal.paymentValue || ""} disabled={disableFields}
                                       onChange={(e) => this.setState({
                                           proposal: {
                                               ...proposal,
                                               paymentValue: e.target.value
                                           }
                                       })}/>
                            </div>
                            <small class="form-text ad-error">{error.paymentValue}</small>
                        </div>
                    </div>

                    <div class="proposals-edit__buttons">
                        {new_m && (
                            <div class="btn dashboard-add__button" onClick={this.submitProposal.bind(this)}>
                                Enviar Proposta
                            </div>)}
                        {edit_m && (proposal.rejected || sent_t) && (
                            <div id="dashboardDeleteButton" class="btn dashboard-add__button"
                                 onClick={this.deleteProposal.bind(this)}>
                                Deletar Proposta
                            </div>)}
                        {edit_m && new_t && !proposal.rejected && (
                            <div>
                                {!proposal.owner && (
                                    <div id="dashboardAcceptButton" class="btn dashboard-add__button"
                                         onClick={this.acceptProposal.bind(this)}>
                                        Aceitar Proposta {/*//TODO don't show this if i am the proposal owner*/}
                                        {/*//TODO show terms of use for contract - the owner can break the contract,...*/}
                                    </div>
                                )}
                                <div class="btn dashboard-add__button" onClick={this.sendProposalRevision.bind(this)}>
                                    Enviar Revisao
                                </div>
                                <div id="dashboardDeleteButton" class="btn dashboard-add__button"
                                     onClick={this.rejectProposal.bind(this)}>
                                    Rejeitar Proposta
                                </div>
                                {/*//TODO add a confirm modal to 'reject proposal'*/}
                            </div>)}
                    </div>
                </div>
            </div>
        )
    }
}