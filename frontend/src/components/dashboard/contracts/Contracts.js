import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";
import {AdAxiosGet} from "../../../auth";
import {HOST} from "../../../configs";

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
                                            {contract.id}
                                        </div>
                                    ))}
                                </div>
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