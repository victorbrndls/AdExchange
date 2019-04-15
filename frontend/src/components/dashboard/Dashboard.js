import {Component} from "preact";
import {Match, Link} from 'preact-router/match'
import Header from "../Header";

export default class Dashboard extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <div>
                <h1>Dashboard</h1>
                <Link href="/dashboard/proposals">Proposals</Link>
                <Link href="/dashboard/contracts">Contracts</Link>

                <Match path="/dashboard/proposals">
                    {({matches}) => matches && (
                        <div>
                            Proposals
                        </div>
                    )}
                </Match>
                <Match path="/dashboard/contracts">
                    {({matches}) => matches && (
                        <div>
                            Contracts
                        </div>
                    )}
                </Match>
            </div>
        )
    }
}