import {Component} from "preact";
import {API_URL} from "../configs";

export default class Header extends Component {
    constructor({url}) {
        super();

        this.updateAuthMode(url);
    }

    componentDidMount() {
        document.getElementById('authSubmit').addEventListener('click', this.createAccount, false);
    }

    componentWillUpdate({url}) {
        this.updateAuthMode(url);
    }

    createAccount() {
        let formData = new FormData();

        fetch(new Request(`${API_URL}/v1/auth/account`,
            {
                method: 'POST',
                body: formData
            })).then((response) => {

        })
    }

    updateAuthMode = (url) => {
        // TODO update url
        if (url.includes('register')) {
            this.setState({mode: 'sign-up'});
        } else {
            this.setState({mode: 'sign-in'});
        }
    };

    render(props, {mode}) {
        return (
            <div style="display: flex; justify-content: center;">
                <div id="auth" class="shadow mt-5">
                    <div class="auth-sign-container">
                        <div class={"auth-sign " + (mode === 'sign-in' ? " active" : "")}
                             style="border-radius: .25rem 0 0 0" onClick={() => {
                            this.setState({mode: 'sign-in'});
                        }}>Sign In
                        </div>
                        <div class={"auth-sign " + (mode === 'sign-up' ? " active" : "")}
                             style="border-radius: 0 .25rem 0 0" onClick={() => {
                            this.setState({mode: 'sign-up'});
                        }}>Sign Up
                        </div>
                    </div>
                    <div style="margin: 22px 12px;">
                        <div class="text-center" onClick="">
                            <div class="form-group">
                                <input type="email" class="form-control" id="authEmailField"
                                       aria-describedby="emailHelp" placeholder="Enter email"/>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" id="authPasswordField"
                                       placeholder="Password"/>
                            </div>

                            <button id="authSubmit" class="btn btn-primary">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}