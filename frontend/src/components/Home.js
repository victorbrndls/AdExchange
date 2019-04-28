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

                <div> {/*//TEMP*/}
                    <div
                        onClick={() => localStorage.setItem("adExchange.authToken", "eca70362be2f4b3eb7013210fd71e940cdc3a0a86bf743de88a344a70848a48bc5a58e9d65f443a7b1e9ccd62ef33b204aca4756fdf84e8cba3629a02f49d90f")}>
                        a@a
                    </div>
                    <div
                        onClick={() => localStorage.setItem("adExchange.authToken", "8e9d65f443a7b1e9cceca70362be2f4b3eb7013210fd71e86bf743de88a344a70848a48bc5a5df84e8cba36d62ef33b204aca4756fc3a0a29a02f49d90f940cd")}>
                        b@b
                    </div>
                </div>

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