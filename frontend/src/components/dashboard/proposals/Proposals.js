import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";
import AddProposal from "./EditProposal";
import {AdAxiosGet} from "../../../auth";
import {HOST} from "../../../configs";

export default class Proposals extends Component {
    constructor(props) {
        super(props);

        this.state = {
            newProposals: [],
            sentProposals: []
        };

        this.hasRequestedProposals = false;

        this.requestProposals();
    }

    requestProposals() {
        if (!this.hasRequestedProposals) {
            this.hasRequestedProposals = true;

            AdAxiosGet.get(`${HOST}/api/v1/proposals/me?embed=website`).then((response) => {
                this.setState(
                    {
                        newProposals: response.data.new,
                        sentProposals: response.data.sent
                    }
                );
            });
        }
    }

    reload() {
        this.hasRequestedProposals = false;
        this.requestProposals();
    }

    render({}, {newProposals, sentProposals}) {
        return (
            <div>
                <Match path={"/dashboard/proposals"} not>
                    <LeftArrow cb={() => route('/dashboard/proposals')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/proposals" exact>
                        <div>
                            <div>
                                <div class="proposal-type__container">
                                    <span class="proposal-type__header">Recebidas</span>
                                </div>
                                <div>
                                    {newProposals.map((proposal) => (
                                        <div>
                                            <Proposal {...proposal} type="NEW"/>
                                        </div>
                                    ))}
                                    {newProposals.length === 0 && (
                                        <div class="proposal__none">Nenhuma no momento</div>
                                    )}
                                </div>
                            </div>
                            <div>
                                <div class="proposal-type__container">
                                    <span class="proposal-type__header">Enviadas</span>
                                </div>
                                <div>
                                    {sentProposals.map((proposal) => (
                                        <div>
                                            <Proposal {...proposal} type="SENT"/>
                                        </div>
                                    ))}
                                    {sentProposals.length === 0 && (
                                        <div class="proposal__none">Nenhuma no momento</div>
                                    )}
                                </div>
                            </div>

                        </div>
                    </Match>

                    <Match path="/dashboard/proposals/edit" include>
                        <AddProposal reload={this.reload.bind(this)}/>
                    </Match>
                </div>
            </div>
        )
    }
}

// TODO improve request performance, make less request to get the website name
class Proposal extends Component {
    constructor(props, state) {
        super(props);

        this.website = props.website;
    }

    render({id, creationDate, type, version, rejected}) {
        // TODO display the creator name
        return (
            <div class="proposal shadow">
                <div>
                    <div>
                        <span class="mr-3">Proposta para "{this.website.name}"</span>
                        <span class="badge badge-secondary mr-3" style="font-variant: small-caps;">v {version}</span>
                        {rejected && (<span class="badge badge-danger">Rejeitada</span>)}
                    </div>
                    <div class="text-muted" style="font-size: 13px; margin-top: 2px;">
                        {type === 'NEW' ? (
                            <span style="margin-right: 50px;">De: Alguem Para Min</span>) : ""}
                        <span>Enviada em: {new Date(creationDate).toLocaleDateString()}</span>
                    </div>
                </div>
                <div>
                    <div class="dashboard-website__rounded-button proposal-type__button"
                         onClick={() => route(`/dashboard/proposals/edit/${id}?type=${type}`)}>Ver Proposta
                    </div>
                </div>
            </div>
        )
    }
}