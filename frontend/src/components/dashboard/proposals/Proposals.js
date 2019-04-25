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
            proposals: []
        };

        this.requestProposals();
    }

    requestProposals() {
        AdAxiosGet.get(`${HOST}/api/v1/proposals/me`).then((response) => {
            this.setState(
                {proposals: response.data}
            );
        });
    }

    render({}, {proposals}) {
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
                            </div>
                            <div>
                                <div class="proposal-type__container">
                                    <span class="proposal-type__header">Enviadas</span>
                                </div>
                                <div>
                                    {proposals.map((proposal) => (
                                        <Proposal {...proposal}/>
                                    ))}
                                </div>
                            </div>

                        </div>
                    </Match>

                    <Match path="/dashboard/proposals/edit" include>
                        <AddProposal/>
                    </Match>
                </div>
            </div>
        )
    }
}

// TODO improve request performance, make less request to get the website name
class Proposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            websiteName: ""
        };

        this.requestWebsiteInformation();
    }

    requestWebsiteInformation() {
        if (this.props.websiteId === null)
            return;

        AdAxiosGet.get(`${HOST}/api/v1/websites/${this.props.websiteId}`).then((response) => {
            this.setState({websiteName: response.data.name});
        });

    }

    render({id, websiteId, adId, duration, paymentMethod, paymentValue, creationDate}, {websiteName}) {
        // TODO display the creator name
        return (
            <div class="proposal shadow">
                <div>
                    <span>Proposta para "{websiteName}"</span>
                    <div class="text-muted" style="font-size: 13px; margin-top: 2px;">
                        <span style="margin-right: 50px;">De: {websiteId}</span>
                        <span>Enviada: {new Date(creationDate).toLocaleDateString()}</span>
                    </div>
                </div>
                <div>
                    <div class="dashboard-website__rounded-button proposal-type__button">Ver Proposta</div>
                </div>
            </div>
        )
    }
}