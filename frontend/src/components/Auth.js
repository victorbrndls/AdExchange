import {Component} from "preact";
import {route} from 'preact-router';
import {login, logout, createAccount, auth} from "../auth";

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
        if (!this.validateFields())
            return;

        createAccount(this.state.email, this.state.password).then(() => {
            route('/');
        }).catch((response) => {
            switch (response) {
                case "EMAIL_ALREADY_EXISTS":
                    this.setState({error: {...this.state.error, email: "Esse email ja' existe"}});
                    return;
                case "INVALID_EMAIL":
                    this.setState({error: {...this.state.error, email: "Esse email n~ao e' valido"}});
                    return;
                case "INVALID_PASSWORD":
                    this.setState({error: {...this.state.error, email: "Esse senha e' invalida"}});
                    return;
            }
        });
    }

    login() {
        this.setState({error: {}});

        login(this.state.email, this.state.password).then(() => {
            route('/dashboard');
            location.reload();
        }).catch((response) => {
            switch (response) {
                case "FAIL":
                    this.setState({error: {...this.state.error, password: "Email ou senha incorreto"}});
                    return;
            }
        });
    }

    /**
     * @return {Boolean} TRUE if the fields required to create an account are valid
     */
    validateFields() {
        this.setState({error: {}});

        if (this.state.password.length < 5) {
            this.setState({error: {...this.state.error, password: "A senha deve ter pelo menos 5 caracteres"}});
            return false;
        }

        return true;
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
            if (url.includes("/logout"))
                logout();

            route('/');
            location.reload();
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
                                        <small>{error.email}</small>
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
                                        <small>{error.password}</small>
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