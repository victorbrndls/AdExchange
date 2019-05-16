import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";
import AccountManager from "../../../managers/AccountManager";

export default class Account extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: "",
            email: "",
            password: ""
        };

        this.hasRequestedAccount = false;

        this.requestAccount();
    }

    requestAccount(){
        if(!this.hasRequestedAccount){
            this.hasRequestedAccount = true;
            AccountManager.getMyAccount().then((response)=>{
                this.setState({name: response.name || ""});
                this.setState({email: response.email || ""});
            })
        }
    }

    // Saves names,
    saveInformation(){

    }

    // Saves email and password
    saveEmailAndPassword() {

    }

    render({}, {name, email, password}) {
        return (
            <div>
                <Match path={"/dashboard/account"} not>
                    <LeftArrow cb={() => route('/dashboard/account')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/account" exact>
                        <div class="mt-4">
                            <div class="mb-4 shadow p-2 rounded bg-white">
                                <div class="form-group">
                                    <label class="font-weight-bold">Nome</label>
                                    <input type="text" class="form-control" placeholder="Joao Silva da Luz" value={name}
                                           onChange={(e) => this.setState({name: e.target.value})}/>
                                    <small class="form-text text-muted">Essa nome é utilizado por outras pessoas para
                                        identificar voce
                                    </small>
                                </div>
                                <div id="dashboardAccountInfoSave" class="btn dashboard-add__button" onClick={this.saveInformation.bind(this)}>
                                    Salvar
                                </div>
                            </div>

                            <div class="shadow p-2 rounded bg-white">
                                <div class="form-group">
                                    <label class="font-weight-bold">Email</label>
                                    <input type="email" class="form-control" placeholder="Email" value={email}
                                           onChange={(e) => this.setState({email: e.target.value})}/>
                                </div>

                                <div class="form-group">
                                    <label class="font-weight-bold">Senha</label>
                                    <input type="password" class="form-control" placeholder="*********" value={password}
                                           onChange={(e) => this.setState({password: e.target.value})}/>
                                </div>

                                <div class="btn dashboard-add__button" onClick={this.saveEmailAndPassword.bind(this)}>
                                    Salvar
                                </div>
                            </div>
                        </div>
                    </Match>
                </div>
            </div>
        )
    }
}