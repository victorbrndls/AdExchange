import {Component} from "preact";
import {Website} from "../websites/Websites";
import Axios from "axios";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost, auth} from "../../../auth";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import {Link, route} from "preact-router";
import UrlUtils from "../../utils/UrlUtils";
import {ConfirmationModal} from "../../utils/Components";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            mode: "NEW", // EDIT or NEW
            type: null, // NEW or SENT
            proposal: {
                id: null,
                websiteId: null,
                website: null,
                adId: null,
                ad: null,
                duration: null,
                paymentMethod: 'PAY_PER_CLICK',
                paymentValue: null,
                rejected: false
            },
            ads: []
        };

        this.updateMode();

        this.requestProposalInformation().then(() => {
            this.requestWebsiteInformation();
            this.requestAdsInformation();
        });
    }

    /**
     * The proposal page can be in one of two modes.
     *     'NEW': The user is creating a new proposal.
     *     'EDIT': The user is editing an existing proposal. The 'EDIT' mode has two types.
     *          'SENT': The  user sent the proposal to somebody else and is viewing the proposal but can't edit it.
     *          'NEW': The user is viewing the proposal that was sent to him/her, he/she can edit it.
     */
    updateMode() {
        let search = new URLSearchParams(location.search);

        if (search.get('mode') !== 'NEW')
            this.setState({mode: 'EDIT'});


        if (this.state.mode === 'EDIT') {
            this.setState({type: search.get('type')});
            this.setState({proposal: {...this.state.proposal, id: location.pathname.split("edit/")[1]}})
        } else if (this.state.mode === 'NEW') {
            this.setState({proposal: {...this.state.proposal, websiteId: search.get('websiteId')}})
        }
    }

    async requestProposalInformation() {
        if (this.state.proposal.id) {
            let res = await AdAxiosGet.get(`${HOST}/api/v1/proposals/${this.state.proposal.id}`);
            this.setState({proposal: res.data});
        }
    }

    requestWebsiteInformation() {
        let wId = this.state.proposal.websiteId;

        if (!wId) {
            this.setState({error: {...error, website: "ID do website inválido"}});
        } else {
            AdAxiosGet.get(`${HOST}/api/v1/websites/${wId}`).then((response) => {
                this.setState({proposal: {...this.state.proposal, website: response.data}});
            });
        }
    }

    requestAdsInformation() {
        if (this.state.mode === 'NEW') {
            AdAxiosGet.get(`${HOST}/api/v1/ads/me?embed=parsedOutput`).then((response) => {
                this.setState({ads: response.data});
            });
        } else if (this.state.mode === 'EDIT') {
            if (this.state.proposal.adId) {
                AdAxiosGet.get(`${HOST}/api/v1/ads/${this.state.proposal.adId}?embed=parsedOutput`).then((response) => {
                    this.setState({proposal: {...this.state.proposal, ad: response.data}});
                });
            }
        }
    }

    handleAdChange(e) {
        let adId = e.target.value;

        if (adId === "none") {
            this.setState({proposal: {...this.state.proposal, adId: null, ad: null}});
        } else {
            let selectedAd = this.state.ads.filter(ad => ad.id === adId)[0];
            selectedAd ? this.setState({proposal: {...this.state.proposal, ad: selectedAd, adId: adId}}) : undefined;
        }
    }

    submitProposal() {
        let proposal = this.state.proposal;

        let formData = new FormData();
        formData.append("websiteId", proposal.websiteId);
        formData.append("adId", proposal.adId);
        formData.append("duration", proposal.duration);
        formData.append("paymentMethod", proposal.paymentMethod);
        formData.append("paymentValue", proposal.paymentValue);

        AdAxiosPost.post(`${HOST}/api/v1/proposals`, formData).then((response) => {
            route('/dashboard/proposals');
            this.props.reload();
        }).catch((error) => {
            this.handleErrorResponse(error);
        });
    }

    acceptProposal() {
        ConfirmationModal.renderFullScreen("Você tem certeza que quer aceitar essa Proposta?", () => {
            AdAxiosPost.post(`${HOST}/api/v1/proposals/accept/${this.state.proposal.id}`).then((response) => {
                route('/dashboard/contracts');
            });
        });
    }

    sendProposalRevision() {
        let proposal = this.state.proposal;

        let formData = new FormData();
        formData.append("duration", proposal.duration);
        formData.append("paymentMethod", proposal.paymentMethod);
        formData.append("paymentValue", proposal.paymentValue);

        AdAxiosPost.post(`${HOST}/api/v1/proposals/revision/${this.state.proposal.id}`, formData).then((response) => {
            route('/dashboard/proposals');
            this.props.reload();
        }).catch((error) => {
            this.handleErrorResponse(error);
        });
    }

    rejectProposal() {
        ConfirmationModal.renderFullScreen("Você tem certeza que quer rejeitar essa Proposta?", () => {
            AdAxiosPost.post(`${HOST}/api/v1/proposals/reject/${this.state.proposal.id}`).then((response) => {
                route('/dashboard/proposals');
                this.props.reload();
            });
        });
    }

    deleteProposal() {
        ConfirmationModal.renderFullScreen("Você tem certeza que quer deletar essa Proposta?", () => {
            AdAxiosPost.delete(`${HOST}/api/v1/proposals/${this.state.proposal.id}`).then((response) => {
                route('/dashboard/proposals');
                this.props.reload();
            }).catch((error) => {
                this.handleErrorResponse(error);
            });
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
                this.setState({error: {...error, ad: "ID do anúncio inválido, tente outro"}});
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
                        paymentValue: "Valor do pagamento inválido. O valor deve ser maior que 0,00 e conter no máximo 2 casas decimais. Use virgula(,) em vez de ponto(.)"
                    }
                });
                return;
            case 'INVALID_PAYMENT_METHOD':
                this.setState({error: {...error, paymentMethod: "Método de pagamento inválido"}});
                return;
        }
    }

    render({}, {proposal, error, ads, mode, type}) {
        let edit_m = mode === 'EDIT';
        let new_m = mode === 'NEW';
        let sent_t = type === 'SENT';
        let new_t = type === 'NEW';

        let disableFields = sent_t || proposal.rejected;

        return (
            <div>
                <div style="font-family: Raleway; font-size: 30px;">
                    Proposta para "{proposal.website ? proposal.website.name : ""}"
                    {proposal.rejected && (<span class="badge badge-danger ml-3">Rejeitada</span>)}
                </div>
                <div>
                    <div style="position: relative;">
                        <div class="blocking-container"/>
                        <Website {...proposal.website}/>
                        <small class="form-text ad-error">{error.website}</small>
                    </div>

                    <div>
                        <div class="form-group websites-add__form">
                            <label>Anúncio</label>
                            <select class="custom-select" onChange={this.handleAdChange.bind(this)}
                                    disabled={edit_m}>
                                <option value="none">Selecione um anúncio</option>
                                {ads && ads.map((ad) => (
                                    <option value={ad.id}>{ad.name}</option>
                                ))}
                            </select>
                            {ads.length === 0 && new_m && (
                                <small class="form-text">Voce não possiu nenhum anúncio.&nbsp;
                                    <Link href="/dashboard/ads/edit?mode=new">
                                        Clique aqui para criar um.
                                    </Link>
                                </small>)}
                            <small class="form-text text-muted">O anúncio da proposta não pode ser editado depois que
                                ela for
                                aceita, isso significa que mesmo que alguma alteração ocorra no anúncio, ela não será
                                visível para as pessoas. Caso você queira alterar o anúncio será necessário cancelar o
                                contrato e fazer outro.
                            </small>
                            <small class="form-text ad-error">{error.ad}</small>
                            <div class="ad-container">
                                <div class="blocking-container"/>
                                {proposal.ad && (
                                    <div class="ads-ad-wrapper mt-4">
                                        {proposal.ad.type === 'TEXT' ? (<TextAd {...proposal.ad}/>) : (
                                            <ImageAd {...proposal.ad}/>)}
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
                            <small class="form-text text-muted">Por quanto tempo o anúncio ficará ativo (de 1 a 365)
                            </small>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Pagamento</label>
                            <select id="p_paymentMethod" class="custom-select"
                                    value={proposal.paymentMethod}
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
                                <input id="p_paymentValue" class="form-control"
                                       placeholder="Valores com no maximo 2 casas decimais (Ex.: 1,50 4,54 0,10 18,01 0,50)"
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
                                        Aceitar Proposta
                                        {/*//TODO show terms of use for contract - the owner can break the contract,...*/}
                                    </div>
                                )}
                                <div class="btn dashboard-add__button"
                                     onClick={this.sendProposalRevision.bind(this)}>
                                    Enviar Revisao
                                </div>
                                <div id="dashboardDeleteButton" class="btn dashboard-add__button"
                                     onClick={this.rejectProposal.bind(this)}>
                                    Rejeitar Proposta
                                </div>
                            </div>)}
                    </div>
                </div>
            </div>
        )
    }
}