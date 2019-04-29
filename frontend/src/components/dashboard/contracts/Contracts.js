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
                                            <Contract {...contract}/>
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

class Contract extends Component {
    constructor(props) {
        super(props);

        this.state = {
            websiteName: ""
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

    static convertDate(date){
        let dt = new Date(date);
        return `${dt.getDate()}/${dt.getUTCMonth()}/${dt.getUTCFullYear()}`;
    }

    render({expiration, paymentMethod, paymentValue}, {websiteName}) {
        return (
            <div class="contract shadow">
                <div class="contract__header">
                    Contrato para "{websiteName}"
                </div>
                <div class="contract__body">
                    <div class="contract__body-item">Válido até {Contract.convertDate(expiration)}</div>
                    <div class="contract__body-item">Pagemento {paymentMethod === 'PAY_PER_CLICK' ? "por Click" : "por Visualizacao"}</div>
                    <div class="contract__body-item">Valor do pagamento R${paymentValue}</div>
                </div>
            </div>
        )
    }
}