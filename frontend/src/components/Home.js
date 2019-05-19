import {Component} from "preact";
import {Link} from "preact-router/match";

import {auth} from "../auth";
import {route} from "preact-router";

export default class Home extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div style="background-color: #f9fafc;">
                <div class="header">
                    <div class="container">
                        <div id="logo">
                            <img src="/assets/logo.png"/>
                        </div>
                        <div class="ae-navbar">
                            <div class="ae-navbar--link">
                                <Link href="/auth?register">Criar Conta</Link>
                            </div>
                            <div class="ae-navbar--link">
                                <Link href="/auth?login">Entrar</Link>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div onClick={() => route('/dashboard/panel')}>Dashbaord</div>
                    </div>
                </div>
            </div>
        )
    }
}

{/*
<div>
    <div
        onClick={() => localStorage.setItem("adExchange.authToken", "eca70362be2f4b3eb7013210fd71e940cdc3a0a86bf743de88a344a70848a48bc5a58e9d65f443a7b1e9ccd62ef33b204aca4756fdf84e8cba3629a02f49d90f")}>
        a@a
    </div>
    <div
        onClick={() => localStorage.setItem("adExchange.authToken", "8e9d65f443a7b1e9cceca70362be2f4b3eb7013210fd71e86bf743de88a344a70848a48bc5a5df84e8cba36d62ef33b204aca4756fc3a0a29a02f49d90f940cd")}>
        b@b
    </div>
</div>*/
}
