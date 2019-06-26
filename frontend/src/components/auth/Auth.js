import {Component} from "preact";
import {route} from 'preact-router';
import {login, logout, createAccount, auth} from "../../auth";
import UrlUtils from "../utils/UrlUtils";
import {SvgGoogle, SvgUser} from "../utils/SvgCollection";

const ENTER_KEY_CODE = 13;

export default class Auth extends Component {
    constructor(props) {
        super(props);

        this.state = {
            mode: 'REGISTER',
            name: "",
            email: "",
            password: "",
            newAccount: false,
            error: {}
        };

        this.updateMode();
    }

    createAccount() {
        createAccount(this.state.email, this.state.password).then(() => {
            route('/auth?login&new_account');
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
            route('/dashboard/panel');
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

        if (location.search.includes('new_account'))
            this.setState({newAccount: true});

    };

    handlePasswordKeyUp(e) {
        if (e.keyCode === ENTER_KEY_CODE)
            this.submit();
    }

    submit() {
        let mode = this.state.mode;

        mode === 'LOGIN' ? this.login() : this.createAccount();
    }

    render({url}, {mode, name, email, password, error, newAccount}) {
        if (auth.isUserAuthenticated())
            if (UrlUtils.include('/logout')) {
                logout();
                route('/');
            }

        return (
            <div class="ae-auth">
                <div class="container">
                    <div class="row" style={{height: 100}}/>

                    <div class="row justify-content-center">
                        <div class="col-12 col-sm-10 d-flex justify-content-center">
                            <div class="ae-auth-container shadow">
                                <div class="mb-5">
                                    <div class="ae-auth__header">
                                        {mode === 'REGISTER' ? 'Criar Conta' : "Login"}
                                    </div>

                                    {newAccount && (
                                        <span class="ae-auth__header--success mt-2">Conta criada com sucesso. Utilize-a para entrar em seu dashboard</span>
                                    )}
                                </div>

                                <div class="mb-4">
                                    {mode === 'REGISTER' && (
                                        <div class="ae-auth--input-container" data-title="Nome">
                                            <input class="ae-auth--input" value={name}
                                                   onChange={(e) => this.setState({name: e.target.value})}/>
                                            {error.name && (
                                                <small
                                                    class="ae-auth--input-msg form-text text-danger">{error.name}
                                                </small>
                                            )}
                                        </div>
                                    )}

                                    <div class="ae-auth--input-container" data-title="Email">
                                        <input class="ae-auth--input" value={email}
                                               onChange={(e) => this.setState({email: e.target.value})}/>
                                        {error.email && (
                                            <small
                                                class="ae-auth--input-msg form-text text-danger">{error.email}
                                            </small>
                                        )}
                                    </div>

                                    <div class="ae-auth--input-container" data-title="Senha">
                                        <input class="ae-auth--input" type="password" value={password}
                                               onChange={(e) => this.setState({password: e.target.value})}
                                               onKeyUp={this.handlePasswordKeyUp.bind(this)}/>
                                        {error.password && (
                                            <small
                                                class="ae-auth--input-msg form-text text-danger">{error.password}
                                            </small>
                                        )}
                                    </div>

                                    {mode === 'LOGIN' && (
                                        <div class="ae-auth--forgot-password-container">
                                            <a href="/auth" class="ae-auth--forgot-password no-decoration">
                                                Esqueceu sua Senha?
                                            </a>
                                        </div>
                                    )}

                                    <div>
                                        <div class="ae-auth--confirm-btn" onClick={this.submit.bind(this)}>
                                            {mode === 'REGISTER' ? 'CRIAR' : "ENTRAR"}
                                        </div>
                                    </div>
                                </div>


                                <div class="ae-auth-divider my-4">
                                    <div class="ae-auth-divider--line"/>
                                    <span class="ae-auth-divider--text">OU</span>
                                    <div class="ae-auth-divider--line"/>
                                </div>

                                {mode === 'REGISTER' && (
                                    <div>
                                        <div class="ae-auth--sign-in-card"
                                             onClick={() => this.setState({mode: 'LOGIN'})}>
                                            <div class="ae-auth--sign-in-card__image-container">
                                                <SvgUser clazz="ae-auth--sign-in-card__image"/>
                                            </div>
                                            <div class="ae-auth--sign-in-card__text">
                                                Já possuo uma conta
                                            </div>
                                        </div>
                                    </div>
                                )}

                                {mode === 'LOGIN' && (
                                    <div>
                                        <div class="ae-auth--sign-in-card"
                                             onClick={() => this.setState({mode: 'REGISTER'})}>
                                            <div class="ae-auth--sign-in-card__image-container">
                                                <SvgUser clazz="ae-auth--sign-in-card__image"/>
                                            </div>
                                            <div class="ae-auth--sign-in-card__text">
                                                Criar uma conta
                                            </div>
                                        </div>

                                        {/*<div class="ae-auth--sign-in-card">
                                        <div class="ae-auth--sign-in-card__image-container">
                                            <SvgGoogle clazz="ae-auth--sign-in-card__image"/>
                                        </div>
                                        <div class="ae-auth--sign-in-card__text">
                                            Utilize sua conta Google
                                        </div>
                                    </div>*/}
                                    </div>
                                )}

                            </div>
                        </div>
                    </div>

                    <div class="row" style={{height: 100}}/>
                </div>
            </div>
        )
    }
}