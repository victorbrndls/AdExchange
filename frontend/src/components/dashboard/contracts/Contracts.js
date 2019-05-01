import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";
import {AdAxiosGet} from "../../../auth";
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

            AdAxiosGet.get(`${HOST}/api/v1/contracts/me`).then((response) => {
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
            websiteName: "",
            showAd: false,
            ad: null
        };

        this.requestWebsiteInformation();
    }

    requestWebsiteInformation() {
        if (this.props.websiteId === undefined)
            return;

        AdAxiosGet.get(`${HOST}/api/v1/websites/${this.props.websiteId}`).then((response) => {
            this.setState({websiteName: response.data.name});
        });
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

    static convertDate(date) {
        let dt = new Date(date);
        return `${dt.getDate()}/${dt.getUTCMonth() + 1}/${dt.getUTCFullYear()}`;
    }

    render({expiration, paymentMethod, paymentValue}, {websiteName, showAd, ad}) {
        return (
            <div class="contract shadow">
                <div class="contract__header">
                    Contrato para "{websiteName}"
                </div>
                <div class="contract__body">
                    <div class="contract__body-item">Válido até {Contract.convertDate(expiration)}</div>
                    <div class="contract__body-item">
                        {PaymentMethod[paymentMethod]}</div>
                    <div class="contract__body-item">Valor do pagamento R${paymentValue}</div>
                    <div class="contract__body-item">
                        <div class="contract__body-show_ad" onClick={this.handleShowAd.bind(this)}>Ver Anuncio
                        </div>
                        {showAd && ad !== null && (
                            <div style="justify-content: center; display: flex; position: relative;">
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