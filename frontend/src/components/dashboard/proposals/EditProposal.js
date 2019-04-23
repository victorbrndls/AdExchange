import {Component} from "preact";
import {Website} from "../websites/Websites";
import Axios from "axios";
import {HOST} from "../../../configs";
import {auth} from "../../../auth";
import Ad from "../../Ad";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
            mode: "EDIT", // EDIT or NEW
            proposalId: parseInt(Math.random() * 1000),
            website: {},
            adId: undefined
        };

        this.updateMode();

        this.requestWebsiteInformation();
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

    requestWebsiteInformation() {
        let id = this.getWebsiteId();

        if (id === -1) {
            this.setState({error: "websiteId invalido"});
            return;
        }

        Axios.get(`${HOST}/api/v1/websites/${id}`, {
            params: {
                token: auth.getToken()
            }
        }).then((response) => {
            this.setState({website: response.data});
        });
    }

    render({}, {website, proposalId, error}) {
        return (
            <div>
                <div style="font-family: Raleway; font-size: 30px;">
                    Proposta #{proposalId}
                </div>
                <div>
                    <div>
                        WebsiteId: {this.getWebsiteId()}
                    </div>

                    <div style="position: relative;">
                        <div class="blocking-container"/>
                        <Website {...website}/>
                    </div>

                    <div>
                        <div>
                            <div class="form-group websites-add__form">
                                <label>Anúncio</label>
                                <select class="custom-select">
                                    <option>Anúncio #1</option>
                                    <option>Anúncio #2</option>
                                </select>
                                <div class="mb-4"/>
                                <div style="justify-content: center; display: flex;">
                                    <Ad/>
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
                                    <input class="form-control" aria-label="Valor" placeholder="1.50"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}