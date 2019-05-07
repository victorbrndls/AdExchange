import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow, TextChangerInput} from "../../utils/Components";
import {route} from "preact-router";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import {HOST} from "../../../configs";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import PaymentMethod from "../../utils/PaymentMethod";

export default class Contracts extends Component {
    constructor(props) {
        super(props);

        this.state = {
            contracts: null
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
                            {contracts !== null && (
                                <div>
                                    {contracts.map((contract) => (
                                        <div>
                                            <Contract {...contract}/>
                                        </div>
                                    ))}
                                </div>
                            )}
                            {contracts !== null && contracts.length === 0 && (
                                <div class="proposal__none">Nenhum no momento</div>
                            )}
                        </div>
                    </Match>

                    {/*<Match path="/dashboard/contracts/edit" include>*/}
                    {/**/}
                    {/*</Match>*/}
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

        AdAxiosGet.get(`${HOST}/api/v1/ads/${this.props.adId}`).then((response) => {
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

            AdAxiosPost.patch(`${HOST}/api/v1/contracts/${this.state.id}`, formData).then((response) => {

            });
        }
    }

    /**
     * @param date {String} the date returned by the server is in UTC format
     * @return {string}
     */
    static convertDate(date) {
        let dt = new Date(date + 'Z'); // 'Z' means UTC (http://www.ecma-international.org/ecma-262/5.1/#sec-15.9.1.15)

        return {
            expired: dt.getTime() < Date.now(),
            date: `${dt.getDate()}/${dt.getUTCMonth() + 1}/${dt.getUTCFullYear()}`
        };
    }

    render({expiration, paymentMethod, paymentValue, acceptorContractName, creatorContractName, website}, {showAd, ad}) {
        let contractName = acceptorContractName || creatorContractName;

        let contractExpiration = Contract.convertDate(expiration);

        return (
            <div class="contract shadow">
                <div class="contract__header">
                    <TextChangerInput value={contractName} cb={(newName) => {
                        this.updateContractName(newName)
                    }}/>
                </div>
                <div class="contract__body text-muted">
                    <div class="contract__body-item">Website:
                        <span class="font-italic">{website ? website.name : "Erro"}</span></div>
                    <div class={`contract__body-item ${contractExpiration.expired ? "contract__expired" : ""}`}>Válido até {contractExpiration.date}</div>
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