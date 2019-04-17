import {Component} from "preact";
import {HOST} from "../configs";
//import * as axios from "axios";
import Axios from "axios";

export default class Header extends Component {
    constructor(props) {
        super(props);

        this.updateAuthMode(props.url);
    }

    createAccount() {
        if(!this.validateFields())
            return;

        let fields = this.getFields();

        let formData = new FormData();
        formData.append('email', fields.email);
        formData.append('password', fields.password);

        Axios.post(`${HOST}/auth/account`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then((response)=>{
           console.log(response);
        });
    }

    login() {

    }

    getFields(){
        return {
            email: document.getElementById('authEmailField').value,
            password: document.getElementById('authPasswordField').value
        }
    }

    /**
     * @return {Boolean} TRUE if the fields required to create an account are valid
     */
    validateFields(){
        let field = this.getFields();

        this.setState({passwordError: undefined});
        this.setState({emailError: undefined});

        if(field.password.length < 5){
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

    render({}, {mode}) {
        return (
            <div style="display: flex; justify-content: center;">
                <div id="auth" class="shadow mt-5">
                    <div class="auth-sign-container">
                        <div class={"auth-sign " + (mode === 'sign-in' ? " active" : "")}
                             style="border-radius: .25rem 0 0 0" onClick={() => {
                            this.setState({mode: 'sign-in'});
                        }}>Entrar
                        </div>
                        <div class={"auth-sign " + (mode === 'sign-up' ? " active" : "")}
                             style="border-radius: 0 .25rem 0 0" onClick={() => {
                            this.setState({mode: 'sign-up'});
                        }}>Criar
                        </div>
                    </div>
                    <div style="margin: 22px 12px;">
                        <div class="text-center">
                            <div class="form-group">
                                <input type="email" class="form-control" id="authEmailField"
                                       aria-describedby="emailHelp" placeholder="Email"/>
                                {this.state.emailError && (
                                    <small>{this.state.emailError}</small>
                                )}
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" id="authPasswordField"
                                       placeholder="Senha"/>
                                {this.state.passwordError && (
                                    <small>{this.state.passwordError}</small>
                                )}
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