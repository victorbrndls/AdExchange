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
                <h2>Home page</h2>

                <Link href="/auth/register">Register</Link>
                <br/>
                <Link href="/auth/login">Login</Link>

                <div>
                    auth: {auth.getToken()}
                </div>
            </div>
        )
    }
}