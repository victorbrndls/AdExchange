import {Component} from "preact";
import {Link} from "preact-router/match";

export default class Header extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div>
                <h2>Home page</h2>


                <Link href="/auth/register">Register</Link>
                <Link href="/auth/login">Login</Link>
            </div>
        )
    }
}