import {Component} from "preact";
import {Link} from 'preact-router/match'

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
                <nav style="display: inline-block; align-self: center;">
                    <Link href="/">Home</Link>
                    <Link href="/dashboard">Dashboard</Link>
                </nav>
            </div>
        )
    }
}