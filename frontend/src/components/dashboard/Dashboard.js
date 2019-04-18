import {Component} from "preact";
import {Link} from 'preact-router/match'
import Websites from './Websites';
import Match from '../utils/Match';

export default class Dashboard extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div id="dashboard" class="h-100">
                <div class="dashboard__sidebar">
                    <div class="dashboard__sidebar-logo">
                        <span>AdExchange</span>
                    </div>
                    <div class="dashboard__sidebar-bar">
                        <div class="dashboard__sidebar--item">
                            <Link href="/dashboard/websites" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Websites</div>
                            </Link>
                        </div>
                        <div class="dashboard__sidebar--item">
                            <Link href="/dashboard/contracts" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Proposals</div>
                            </Link>
                        </div>
                        <div class="dashboard__sidebar--item">
                            <Link href="/dashboard/proposals" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Contracts</div>
                            </Link>
                        </div>
                    </div>
                </div>
                <div class="dashboard__main">
                    <div class="dashboard__main-topbar">

                    </div>
                    <div class="dashboard__main-content">
                        <div style="height: 100%; padding: 16px;">
                            <Match path="/dashboard/websites">
                                <Websites/>
                            </Match>
                            <Match path="/dashboard/contracts">

                            </Match>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
