import {Component} from "preact";
import {Link} from 'preact-router/match'
import Websites from './websites/Websites';
import Match from '../utils/Match';
import Proposals from "./proposals/Proposals";
import Ads from "./ads/Ads";

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
                            <Link href="/dashboard/proposals" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Propostas</div>
                            </Link>
                        </div>
                        <div class="dashboard__sidebar--item">
                            <Link href="/dashboard/contracts" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Contratos</div>
                            </Link>
                        </div>
                        <div class="dashboard__sidebar--item">
                            <Link href="/dashboard/ads" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Anúncios</div>
                            </Link>
                        </div>
                    </div>
                </div>
                <div class="dashboard__main">
                    <div class="dashboard__main-topbar">
                    </div>
                    <div class="dashboard__main-content">
                        <div style="height: 100%; padding: 16px;">

                            <Match path="/dashboard/websites" include>
                                <Websites/>
                            </Match>

                            <Match path="/dashboard/proposals" include>
                                <Proposals/>
                            </Match>

                            <Match path="/dashboard/ads" include>
                                <Ads/>
                            </Match>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
