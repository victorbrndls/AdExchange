import {Component} from "preact";
import {Link} from 'preact-router/match'
import {auth} from "../auth";

export default class Header extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div id="header">
                <h2 style="color: #fff; display: inline-block;">
                    Header
                </h2>
                <nav id="header-nav">
                    <Link href="/">Home</Link>
                    {auth.isUserAuthenticated() && (<Link href="/dashboard">Dashboard</Link>)}
                    {auth.isUserAuthenticated() && (<Link href="/auth/logout">Log out</Link>)}
                </nav>
            </div>
        )
    }
}