import {Component} from "preact";
import {Match} from 'preact-router/match'

export default class Header extends Component {
    constructor() {
        super();
    }

    render({url}) {
        return (
            <div style="display: flex; justify-content: center;">
                <div id="auth" class="shadow mt-5">
                    <div class="auth-sign-container">
                        <div class="auth-sign">Sign In</div>
                        <div class="auth-sign">Sign Up</div>
                    </div>
                    <div style="margin: 12px;">
                        <form class="text-center">
                            <div class="form-group">
                                <input type="email" class="form-control" id="authEmailField"
                                       aria-describedby="emailHelp" placeholder="Enter email"/>
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" id="authPasswordField"
                                       placeholder="Password"/>
                            </div>

                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}