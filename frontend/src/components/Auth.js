import {Component} from "preact";
import {route} from 'preact-router';
import {login, logout, createAccount, auth} from "../auth";
import UrlUtils from "./utils/UrlUtils";

export default class Auth extends Component {
    constructor(props) {
        super(props);

        this.state = {
            mode: 'REGISTER',
            email: "",
            password: "",
            error: {}
        };

        this.updateMode();
    }

    createAccount() {
        createAccount(this.state.email, this.state.password).then(() => {
            route('/auth?login');
            location.reload();
        }).catch((response) => {
            this.setState({error: {}});

            switch (response) {
                case "EMAIL_ALREADY_EXISTS":
                    this.setState({error: {...this.state.error, email: "Esse email já existe"}});
                    return;
                case "INVALID_EMAIL":
                    this.setState({error: {...this.state.error, email: "Esse email não é válido"}});
                    return;
                case "INVALID_PASSWORD":
                    this.setState({error: {...this.state.error, password: "Esse senha não é válida"}});
                    return;
            }
        });
    }

    login() {
        login(this.state.email, this.state.password).then(() => {
            route('/dashboard');
//            location.reload();
        }).catch((response) => {
            this.setState({error: {}});

            switch (response) {
                case "FAIL":
                    this.setState({error: {...this.state.error, password: "Email ou senha incorretos"}});
                    return;
            }
        });
    }

    updateMode = () => {
        if (location.search.includes('register')) {
            this.setState({mode: 'REGISTER'});
        } else {
            this.setState({mode: 'LOGIN'});
        }
    };

    render({url}, {mode, email, password, error}) {
        if (auth.isUserAuthenticated()) {
            if (UrlUtils.include('/logout'))
                logout();

            route('/');
            return;
        }

        return (
            <div class="auth-background">
                <div>
                    <div id="auth">
                        <div class="auth-sign-container">
                            <div class={`auth-sign ${mode === 'LOGIN' ? 'active' : ''}`}
                                 style="border-radius: .25rem 0 0 0" onClick={() => {
                                this.setState({mode: 'LOGIN'});
                            }}>Entrar
                            </div>
                            <div class={`auth-sign ${mode === 'REGISTER' ? 'active' : ''}`}
                                 style="border-radius: 0 .25rem 0 0" onClick={() => {
                                this.setState({mode: 'REGISTER'});
                            }}>Criar
                            </div>
                        </div>
                        <div>
                            <div style="margin: 22px;">
                                <div class="input-group mb-4">
                                    <div class="auth-input-container">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">Email</span>
                                        </div>
                                        <input type="text" class="form-control" value={email}
                                               onChange={(e) => this.setState({email: e.target.value})}/>
                                    </div>
                                    {error.email && (
                                        <small class="mt-1">{error.email}</small>
                                    )}
                                </div>
                                <div class="input-group mb-4">
                                    <div class="auth-input-container">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">Senha</span>
                                        </div>
                                        <input type="password" class="form-control" value={password}
                                               onChange={(e) => this.setState({password: e.target.value})}/>
                                    </div>
                                    {error.password && (
                                        <small class="mt-1">{error.password}</small>
                                    )}
                                </div>
                            </div>

                            <button id="authSubmit" class="btn btn-primary"
                                    onClick={mode === 'LOGIN' ? this.login.bind(this) : this.createAccount.bind(this)}>
                                Enviar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}