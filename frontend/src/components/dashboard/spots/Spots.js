import {Component} from "preact";
import {route} from "preact-router";
import {ConfirmationModal, LeftArrow} from "../../utils/Components";
import Match from "../../utils/Match";
import EditSpot from "./EditSpot";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import {HOST} from "../../../configs";
import PaymentMethod from "../../utils/PaymentMethod";
import {ImageAd, TextAd} from "../ads/CreateAdd";
import {hasContractExpired} from "../contracts/Contracts";

export default class Spots extends Component {
    constructor(props) {
        super(props);

        this.state = {
            spots: [],
            contracts: {},
            ads: {}
        };

        this.hasLoadedSpots = false;
    }

    requestSpots() {
        if (!this.hasLoadedSpots) {
            this.hasLoadedSpots = true;

            AdAxiosGet.get(`${HOST}/api/v1/spots/me`).then((response) => {
                this.setState({spots: response.data});

                this.requestContractsInformation();
                this.requestAdsInformation();
            })
        }
    }

    reload() {
        this.hasLoadedSpots = false;
        this.requestSpots();
    }


    requestContractsInformation() {
        let ids = this.buildBatchRequestString(this.state.spots, 'contractId');

        AdAxiosGet.get(`${HOST}/api/v1/contracts/batch?ids=${ids}`).then((response) => {
            this.setState({contracts: this.mapIdToObject(response.data)});
        });
    }

    requestAdsInformation() {
        let ids = this.buildBatchRequestString(this.state.spots, 'fallbackAdId');

        AdAxiosGet.get(`${HOST}/api/v1/ads/batch?ids=${ids}`).then((response) => {
            this.setState({ads: this.mapIdToObject(response.data)});
        });
    }

    mapIdToObject(items) {
        let states = {};

        items.forEach((item) => {
            states[item.id] = item;
        });

        return states;
    }

    /**
     * Constructs a String that constrains the {elem} from each array object separated by ','
     * @param array {Array}
     * @param elem {String}
     */
    buildBatchRequestString(array, elem) {
        let set = new Set();

        array.forEach((spot) => set.add(spot[elem]));

        let ids = "";

        let setSize = set.size;
        let idx = 0;

        set.forEach((id) => {
            ids += id;

            if (idx !== setSize - 1) {
                ids += ",";
            }

            idx++;
        });

        return ids;
    }

    render({}, {spots, ads, contracts}) {
        return (
            <div>
                <Match path={"/dashboard/spots"} not>
                    <LeftArrow cb={() => route('/dashboard/spots')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/spots" exact>
                        <div>
                            <div class="spots-add dashboard-website__rounded-button"
                                 onClick={() => route('/dashboard/spots/edit?type=new')}>
                                Criar Spot
                            </div>
                            {this.requestSpots.bind(this)()}
                            {spots.map((spot) => (
                                <Spot spot={spot} contract={contracts[spot.contractId]}
                                      ad={ads[spot.fallbackAdId]} reload={this.reload.bind(this)}/>
                            ))}
                        </div>
                    </Match>

                    <Match path="/dashboard/spots/edit" include>
                        <EditSpot reload={this.reload.bind(this)}/>
                    </Match>
                </div>
            </div>
        )

    }
}

class Spot extends Component {
    constructor(props) {
        super(props);

        this.reload = props.reload;
        this.id = props.spot.id;

        this.state = {
            extended: false,
            contractAd: null,
            fallbackAd: null
        };
    }

    handleExtend() {
        this.setState({extended: !this.state.extended});

        this.afterStateChange();
    }

    afterStateChange() {
        if (this.state.extended) {
            if (this.state.contractAd === null) {
                if (this.props.contract !== undefined) {
                    AdAxiosGet.get(`${HOST}/api/v1/ads/${this.props.contract.adId}`).then((response) => {
                        this.setState({contractAd: response.data});
                    });
                }
            }

            if (this.state.fallbackAd === null) {
                if (this.props.ad !== undefined) {
                    this.setState({fallbackAd: this.props.ad});
                }
            }
        }
    }

    deleteSpot() {
        ConfirmationModal.renderFullScreen("Voce tem certeza que quer deletar esse Spot?", () => {
            AdAxiosPost.delete(`${HOST}/api/v1/spots/${this.id}`).then(() => {
                this.reload();
            });
        });
    }

    render({spot, contract, ad}, {extended, contractAd, fallbackAd}) {
        let adName = ad ? ad.name : 'Nenhum';
        let contractName = contract ? contract.acceptorContractName : "Nenhum";

        let contractExpired = contract ? hasContractExpired(contract.expiration) : false;

        return (
            <div class="contract shadow">
                <div class="contract__header"
                     style="display: flex; justify-content: space-between;">
                    <div>{spot.name}</div>
                    <div>
                        <div class="spot-header__option"
                             onClick={() => route(`/dashboard/spots/edit?id=${spot.id}`)}>Editar
                        </div>
                        <div class="spot-header__option"
                             onClick={this.deleteSpot.bind(this)}>Deletar
                        </div>
                    </div>
                </div>
                <div class="contract__body text-muted">
                    <div class="contract__body-item">
                        Id: <span class="spot-id">{spot.id}</span>
                    </div>
                    <div class="contract__body-item">
                        <div>
                            Contrato:&nbsp;
                            <span class={`font-italic ${contractExpired ? 'text-danger' : ''}`}>{contractName}</span>
                            <span class="ml-1 text-danger">{contractExpired ? '- Expirado' : ''}</span>
                        </div>
                        {extended && contractAd && (
                            <AdWrapper ad={contractAd}/>
                        )}
                    </div>
                    <div class="contract__body-item">
                        <div>
                            Anuncio
                            reserva: {adName}
                        </div>
                        {extended && fallbackAd && (
                            <AdWrapper ad={fallbackAd}/>
                        )}
                    </div>
                </div>
                <div style="padding: 0 19px 7px 19px; text-align: center;">
                    <img class={`contract__body-plus ${extended ? "active" : ""}`}
                         src="/assets/expand-arrow.png" onClick={this.handleExtend.bind(this)}/>
                </div>
            </div>
        )
    }
}

const AdWrapper = ({ad}) => (
    <div class="ad-container">
        <div class="ads-ad-wrapper mt-4">
            {ad.type === 'TEXT' ? (<TextAd {...ad}/>) : (
                <ImageAd {...ad}/>)}
        </div>
    </div>
);