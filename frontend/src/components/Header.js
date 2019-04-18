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
                <nav id="header-nav">
                    {auth.isUserAuthenticated() && (<Link href="/dashboard">Dashboard</Link>)}
                    {auth.isUserAuthenticated() && (<Link href="/auth/logout">Log out</Link>)}
                </nav>
            </div>
        )
    }
}