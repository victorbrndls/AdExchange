import {Component} from "preact";
import {Link} from "preact-router/match";

import {auth} from "../auth";

export default class Header extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div>
                <div class="header">
                    AdExchange
                </div>

                {!auth.isUserAuthenticated() && (
                    <div>
                        <Link href="/auth/register">Register</Link>
                        <Link href="/auth/login">Login</Link>
                    </div>
                )}

                <div class="home-card" id="dashboardCard">
                    <Link href="/dashboard">Dashboard</Link>
                </div>

                <div class="home-card" style="background: linear-gradient(45deg, #F57C00, #FFB64D); color: white;">
                    <div>
                        <div>Auth Token:</div>
                        <span style="word-wrap: anywhere;">{auth.getToken()}</span>
                    </div>
                </div>

                <div class="home-card"
                     style="background: rgba(0, 0, 0, 0) linear-gradient(45deg, rgb(83, 0, 245), rgb(225, 77, 255)); color: white; text-align: center;padding: 9px;">
                    <span style="font-family: Raleway; font-size: 21px;">Good things take time to happen, don't worry if this takes more than 4 months to develop</span>
                </div>
            </div>
        )
    }
}