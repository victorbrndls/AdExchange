import {Component} from "preact";
import {route} from "preact-router";
import {ConfirmationModal, LeftArrow} from "../../utils/Components";
import Match from "../../utils/Match";
import EditSpot from "./EditSpot";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import {HOST} from "../../../configs";

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

    deleteSpot(id) {
        ConfirmationModal.renderFullScreen("Voce tem certeza que quer deletar esse Spot?", () => {
            AdAxiosPost.delete(`${HOST}/api/v1/spots/${id}`).then((response) => {
                this.reload();
            });
        });
    }

    requestContractsInformation() {
        let ids = this.buildBatchRequestString(this.state.spots, 'contractId');

        AdAxiosGet.get(`${HOST}/api/v1/contracts/batch?ids=${ids}`).then((response) => {

        });
    }

    requestAdsInformation() {
        let ids = this.buildBatchRequestString(this.state.spots, 'adId');

        AdAxiosGet.get(`${HOST}/api/v1/ads/batch?ids=${ids}`).then((response) => {
            let ads = response.data;
            let adsState = {};

            ads.forEach((ad) => {
                adsState[ad.id] = ad;
            });

            this.setState({ads: adsState});
        });
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
                                <div class="contract shadow">
                                    <div class="contract__header"
                                         style="display: flex; justify-content: space-between;">
                                        <div>{spot.name}</div>
                                        <div>
                                            <div class="spot-header__option"
                                                 onClick={() => route(`/dashboard/spots/edit?id=${spot.id}`)}>Editar
                                            </div>
                                            <div class="spot-header__option"
                                                 onClick={this.deleteSpot.bind(this, spot.id)}>Deletar
                                            </div>
                                        </div>
                                    </div>
                                    <div class="contract__body">
                                        <div class="contract__body-item">
                                            Contato: {spot.contractId === '-1' ? 'Nenhum' : spot.contractId}</div>
                                        <div class="contract__body-item">
                                            Anuncio: {spot.adId === '-1' ? 'Nenhum' : ads[spot.adId].name || '-1'}</div>
                                    </div>
                                </div>
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