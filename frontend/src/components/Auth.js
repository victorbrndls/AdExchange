import {Component} from "preact";
import {route} from 'preact-router';
import {login, logout, createAccount, auth} from "../auth";

export default class Auth extends Component {
    constructor(props) {
        super(props);

        this.props.email = () => document.getElementById("authEmailField");
        this.props.password = () => document.getElementById("authPasswordField");

        this.updateAuthMode(props.url);
    }

    componentDidMount() {
        this.props.email().value = "";
        this.props.password().value = "";
    }

    createAccount() {
        if (!this.validateFields())
            return;

        createAccount(this.props.email().value, this.props.password().value).then(() => {
            route('/');
        }).catch((response) => {
            switch (response.error) {
                case "EMAIL_ALREADY_EXISTS":
                    this.setState({emailError: "Esse email ja' existe"});
                    return;
                case "INVALID_EMAIL":
                    this.setState({emailError: "Esse email na~o eh valido"});
                    return;
                case "INVALID_PASSWORD":
                    this.setState({passwordError: "Essa senha na~o eh valida"});
                    return;
            }
        });
    }

    login() {
        this.clearFieldsError();

        login(this.props.email().value, this.props.password().value).then(() => {
            route('/dashboard');
            location.reload();
        }).catch((response) => {
            switch (response.error) {
                case "FAIL":
                    this.setState({passwordError: "Email ou senha incorretos"});
                    return;
            }
        });
    }

    getFields() {
        return {
            email: this.props.email().value,
            password: this.props.password().value
        }
    }

    getFieldsFormData() {
        let fields = this.getFields();

        let formData = new FormData();
        formData.append('email', fields.email);
        formData.append('password', fields.password);

        return formData;
    }

    clearFieldsError() {
        this.setState({passwordError: undefined});
        this.setState({emailError: undefined});
    }

    /**
     * @return {Boolean} TRUE if the fields required to create an account are valid
     */
    validateFields() {
        let field = this.getFields();

        this.clearFieldsError();

        if (field.password.length < 5) {
            this.setState({passwordError: "A senha deve ter pelo menos 5 caracteres"});
            return false;
        }

        return true;
    }

    /**
     * The authentication mode changes the dispaly in the sign-in/sign-up form
     * @param url
     */
    updateAuthMode = (url) => {
        // TODO update url when the state changes
        if (url.includes('register')) {
            this.setState({mode: 'sign-up'});
        } else {
            this.setState({mode: 'sign-in'});
        }
    };

    render({url}, {mode}) {
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
                            <div class={`auth-sign ${mode === 'sign-in' ? 'active' : ''}`}
                                 style="border-radius: .25rem 0 0 0" onClick={() => {
                                this.setState({mode: 'sign-in'});
                            }}>Entrar
                            </div>
                            <div class={`auth-sign ${mode === 'sign-up' ? 'active' : ''}`}
                                 style="border-radius: 0 .25rem 0 0" onClick={() => {
                                this.setState({mode: 'sign-up'});
                            }}>Criar
                            </div>
                        </div>
                        <div>
                            <div style="margin: 22px;">
                                <div class="input-group mb-4">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1">Email</span>
                                    </div>
                                    <input type="text" class="form-control" aria-label="Username"
                                           aria-describedby="basic-addon1"/>
                                    {this.state.emailError && (
                                        <small>{this.state.emailError}</small>
                                    )}
                                </div>
                                <div class="input-group mb-4">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1">Senha</span>
                                    </div>
                                    <input type="password" class="form-control" aria-label="Username"
                                           aria-describedby="basic-addon1"/>
                                    {this.state.passwordError && (
                                        <small>{this.state.passwordError}</small>
                                    )}
                                </div>
                            </div>

                            <button id="authSubmit" class="btn btn-primary"
                                    onClick={this.state.mode === 'sign-in' ? this.login.bind(this) : this.createAccount.bind(this)}>
                                Enviar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}