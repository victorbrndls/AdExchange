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
                            {proposals.length === 0 && (
                                <span>Nenhuma proposta no momento</span>
                            )}

                            {proposals.map((proposal) => (
                                <Proposal {...proposal}/>
                            ))}
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

let Proposal = ({id, websiteId, adId, duration, paymentMethod, paymentValue}) => (
    <div class="proposal shadow">
        <div>
            <span>Proposta para {websiteId}</span>
        </div>
        <div>

        </div>
    </div>
);