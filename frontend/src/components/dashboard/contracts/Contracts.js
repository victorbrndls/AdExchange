import {Component} from "preact";
import Match from "../../utils/Match";
import {ConfirmationModal, LeftArrow, TextChangerInput} from "../../utils/Components";
import {route} from "preact-router";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import {HOST} from "../../../configs";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import PaymentMethod from "../../utils/PaymentMethod";

export default class Contracts extends Component {
    constructor(props) {
        super(props);

        this.state = {
            contracts: []
        };

        this.hasMadeContractRequest = false;
    }

    requestContracts() {
        if (!this.hasMadeContractRequest) {
            this.hasMadeContractRequest = true;

            AdAxiosGet.get(`${HOST}/api/v1/contracts/me?embed=website`).then((response) => {
                this.setState(
                    {contracts: response.data}
                );
            });
        }
    }

    reload() {
        this.setState({contracts: []});
        this.hasMadeContractRequest = false;
        this.requestContracts();
    }

    render({}, {contracts}) {
        return (
            <div>
                <Match path={"/dashboard/contracts"} not>
                    <LeftArrow cb={() => route('/dashboard/contracts')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/contracts" exact>
                        <div>
                            {this.requestContracts.bind(this)()}
                            {contracts.map((contract) => (
                                <div>
                                    <Contract {...contract} reload={this.reload.bind(this)}/>
                                </div>
                            ))}
                            {contracts.length === 0 && (
                                <div class="proposal__none">Nenhum no momento</div>
                            )}
                        </div>
                    </Match>
                </div>
            </div>
        )
    }
}

class Contract extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.id,
            showAd: false,
            ad: null
        };
    }

    requestAdInformation() {
        if (this.props.adId === undefined)
            return;

        AdAxiosGet.get(`${HOST}/api/v1/ads/${this.props.adId}?embed=parsedOutput`).then((response) => {
            this.setState({ad: response.data});
        });
    }

    handleShowAd() {
        this.setState({showAd: !this.state.showAd});

        if (this.state.ad === null) {
            this.requestAdInformation();
        }
    }

    updateContractName(name) {
        if (this.state.id !== undefined && name !== undefined) {
            let formData = new FormData();
            formData.append("name", name);

            AdAxiosPost.patch(`${HOST}/api/v1/contracts/${this.state.id}`, formData);
        }
    }

    deleteContract() {
        ConfirmationModal.renderFullScreen("Voce tem certeza que quer deletar esse Contrato?", () => {
            if (this.state.id) {
                AdAxiosPost.delete(`${HOST}/api/v1/contracts/${this.state.id}`).then((response) => {
                    this.props.reload();
                });
            }
        });
    }

    /**
     * @param date {String} the date returned by the server is in UTC format
     * @return {string}
     */
    static convertDate(date) {
        let dt = convertContractExpirationToUTC(date);

        return {
            expired: hasContractExpired(date),
            date: `${dt.getDate()}/${dt.getMonth() + 1}/${dt.getFullYear()}`
        };
    }

    render({expiration, paymentMethod, paymentValue, acceptorContractName, creatorContractName, website}, {showAd, ad}) {
        let contractName = acceptorContractName || creatorContractName;

        let contractExpiration = Contract.convertDate(expiration);

        return (
            <div class="contract shadow">
                <div class={`contract__header ${contractExpiration.expired ? 'contract__header--expired' : ''}`}>
                    <TextChangerInput value={contractName} cb={(newName) => {
                        this.updateContractName(newName)
                    }}/>
                    {contractExpiration.expired && (
                        <div class="contract-header__option" onClick={this.deleteContract.bind(this)}>Deletar</div>)}
                </div>
                <div class="contract__body text-muted">
                    <div class="contract__body-item">Website:&nbsp;
                        <span class="font-italic">{website ? website.name : "Erro"}</span></div>
                    <div class={`contract__body-item ${contractExpiration.expired ? "contract__expired" : ""}`}>Válido
                        até {contractExpiration.date} {contractExpiration.expired ? ' (Expirado)' : ''}</div>
                    <div class="contract__body-item">
                        {PaymentMethod[paymentMethod]}</div>
                    <div class="contract__body-item">Valor do pagamento R${paymentValue}</div>
                    <div class="contract__body-item">
                        <div class="contract__body-show_ad" onClick={this.handleShowAd.bind(this)}>Ver Anuncio
                        </div>
                        {showAd && ad !== null && (
                            <div class="ad-container">
                                <div class="ads-ad-wrapper mt-4">
                                    {ad.type === 'TEXT' ? (<TextAd {...ad}/>) : (
                                        <ImageAd {...ad}/>)}
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        )
    }
}

function convertContractExpirationToUTC(expiration) {
    return new Date(expiration + 'Z'); // 'Z' means UTC (http://www.ecma-international.org/ecma-262/5.1/#sec-15.9.1.15)
}

/**
 * @param expirationDate {String}
 * @return {boolean}
 */
export function hasContractExpired(expirationDate) {
    return convertContractExpirationToUTC(expirationDate).getTime() < Date.now();
}