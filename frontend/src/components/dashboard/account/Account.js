import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";
import AccountManager from "../../../managers/AccountManager";
import AddBalance from "./AddBalance";

const SUCCESS_CSS = 'form-text text-success d-inline ml-3';

export default class Account extends Component {

    constructor(props) {
        super(props);

        this.state = {
            name: "",
            email: "",
            password: "",
            error: {}
        };

        this.hasRequestedAccount = false;

        this.requestAccount();
    }

    requestAccount() {
        if (!this.hasRequestedAccount) {
            this.hasRequestedAccount = true;
            AccountManager.getMyAccount().then((response) => {
                this.setState({name: response.name || ""});
                this.setState({email: response.email || ""});
            })
        }
    }

    // Saves name,
    saveInformation() {
        this.setState({error: {}});

        AccountManager.saveAccountInfo(this.state.name).then((response) => {
            this.setState({error: {...this.state.error, infoSuccess: "Informações salvas com sucesso"}});

        }).catch((error) => {
            if (error === 'INVALID_ACCOUNT_NAME')
                this.setState({error: {...this.state.error, name: "Nome inválido"}});

        });
    }

    // Saves email and password
    saveEmailAndPassword() {
        this.setState({error: {}});

        AccountManager.saveAccountAuth(this.state.email, this.state.password).then((response) => {
            this.setState({error: {...this.state.error, authSuccess: "Informações salvas com sucesso"}});

        }).catch((error) => {
            if (error === 'INVALID_EMAIL')
                this.setState({error: {...this.state.error, email: "Email inválido"}});

            if (error === 'EMAIL_ALREADY_EXISTS')
                this.setState({error: {...this.state.error, email: "Esse email já existe"}});

            if (error === 'INVALID_PASSWORD')
                this.setState({error: {...this.state.error, password: "Senha inválida"}});

        });
    }

    render({}, {name, email, password, error}) {
        return (
            <div>
                {/*<Match path={"/dashboard/account"} not>
                    <LeftArrow cb={() => route('/dashboard/account')}/>
                </Match>*/}

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/account" exact>
                        <div class="mt-4">
                            <div class="mb-4 shadow p-2 rounded bg-white">
                                <div class="form-group">
                                    <label class="font-weight-bold">Nome</label>
                                    <input type="text" class="form-control" placeholder="Joao Silva da Luz" value={name}
                                           onChange={(e) => this.setState({name: e.target.value})}/>
                                    {error.name && (
                                        <small class="form-text text-danger"> {error.name}</small>
                                    )}
                                    <small class="form-text text-muted">Esse nome é utilizado por outras pessoas para
                                        identificar voce
                                    </small>
                                </div>
                                <div id="dashboardAccountInfoSave" class="btn dashboard-add__button"
                                     onClick={this.saveInformation.bind(this)}>
                                    Salvar
                                </div>
                                {error.infoSuccess && (
                                    <small class={SUCCESS_CSS}> {error.infoSuccess}</small>
                                )}
                            </div>

                            <div class="shadow p-2 rounded bg-white">
                                <div class="form-group">
                                    <label class="font-weight-bold">Email</label>
                                    <input type="email" class="form-control" placeholder="Email" value={email}
                                           onChange={(e) => this.setState({email: e.target.value})}/>
                                    {error.email && (
                                        <small class="form-text text-danger"> {error.email}</small>
                                    )}
                                </div>

                                <div class="form-group">
                                    <label class="font-weight-bold">Senha</label>
                                    <input type="password" class="form-control" placeholder="*********" value={password}
                                           onChange={(e) => this.setState({password: e.target.value})}/>
                                    {error.password && (
                                        <small class="form-text text-danger"> {error.password}</small>
                                    )}
                                </div>

                                <div class="btn dashboard-add__button" onClick={this.saveEmailAndPassword.bind(this)}>
                                    Salvar
                                </div>
                                {error.authSuccess && (
                                    <small class={SUCCESS_CSS}> {error.authSuccess}</small>
                                )}
                            </div>
                        </div>
                    </Match>

                    <Match path="/dashboard/account/add-balance" include>
                        <AddBalance/>
                    </Match>
                </div>
            </div>
        )
    }
}