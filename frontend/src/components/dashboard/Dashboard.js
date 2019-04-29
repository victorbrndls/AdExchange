import {Component} from "preact";
import {Link} from 'preact-router/match'
import Websites from './websites/Websites';
import Match from '../utils/Match';
import Proposals from "./proposals/Proposals";
import Ads from "./ads/Ads";
import Contracts from "./contracts/Contracts";

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
                        <div class="dashboard__sidebar--item">
                            <Link href="/" activeClassName="active">
                                <div class="dashboard__sidebar--item-container">Home</div>
                            </Link>
                        </div>
                        <div class="dashboard__sidebar--item">
                            <Link href="/auth/login" activeClassName="active">
                                <div class="dashboard__sidebar--item-container" onClick={() => localStorage.removeItem("adExchange.authToken")}>Logout</div>
                            </Link>
                        </div>
                    </div>
                </div>
                <div class="dashboard__main">
                    <div class="dashboard__main-topbar">
                    </div>
                    <div class="dashboard__main-content">
                        <div style="height: 100%; padding: 16px;">

                            {/*Remove this in the future*/}
                            <Match path="/dashboard" exact>
                                <div>
                                    <div>How can I develop this feature better or in a different way?<br/></div>
                                    <div>Design Pattern? / Different Architecture?<br/></div>
                                    <div>What is the simplest thing that could possibly work?<br/></div>
									<br/>
									
									Imagens, gráficos, vídeos, listas, links, gifs, infográficos
                                </div>
                            </Match>

                            <Match path="/dashboard/websites" include>
                                <Websites/>
                            </Match>

                            <Match path="/dashboard/proposals" include>
                                <Proposals/>
                            </Match>

                            <Match path="/dashboard/contracts" include>
                                <Contracts/>
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
