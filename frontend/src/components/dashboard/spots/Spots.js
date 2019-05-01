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
            spots: []
        };

        this.hasLoadedSpots = false;
    }

    requestSpots() {
        if (!this.hasLoadedSpots) {
            this.hasLoadedSpots = true;

            AdAxiosGet.get(`${HOST}/api/v1/spots/me`).then((response) => {
                this.setState({spots: response.data});
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

    render({}, {spots}) {
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
                                            <div class="spot-header__option" onClick={this.deleteSpot.bind(this, spot.id)}>Deletar</div>
                                        </div>
                                    </div>
                                    <div class="contract__body">
                                        <div class="contract__body-item">
                                            Contato: {spot.contractId === '-1' ? 'Nenhum' : spot.contractId}</div>
                                        <div class="contract__body-item">
                                            Anuncio: {spot.adId === '-1' ? 'Nenhum' : spot.adId}</div>
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