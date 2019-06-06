import {Component} from "preact";
import {Link} from 'preact-router/match'
import Websites from './websites/Websites';
import Match from '../utils/Match';
import Proposals from "./proposals/Proposals";
import Ads from "./ads/Ads";
import Contracts from "./contracts/Contracts";
import Spots from "./spots/Spots";
import ControlPanel from "./controlPanel/ControlPanel";
import Account from "./account/Account";
import AccountManager from "../../managers/AccountManager";
import {route} from "preact-router";

import '../../assets/font-awesome-4.7.0/css/font-awesome.min.css'
import {Dropdown} from "../utils/Components";
import NotificationCard from "./controlPanel/Notification";

export default class Dashboard extends Component {
    constructor(props) {
        super(props);
    }

    render({}, {}) {
        return (
            <div id="dashboard" class="h-100">
                <div class="dashboard__sidebar">
                    <div class="dashboard__sidebar-logo">
                        <img src="/assets/logo.png"/>
                    </div>
                    <div class="dashboard__sidebar-bar d-flex flex-column">
                        <div>
                            <div class="dashboard__sidebar--item">
                                <Link href="/dashboard/panel" activeClassName="active">
                                    <div class="dashboard__sidebar--item-container">Dashboard</div>
                                </Link>
                            </div>
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
                                <Link href="/dashboard/spots" activeClassName="active">
                                    <div class="dashboard__sidebar--item-container">Spots</div>
                                </Link>
                            </div>
                        </div>
                        <div class="flex-grow-1 d-flex flex-column justify-content-end">
                            <div class="dashboard__sidebar--item">
                                <Link href="/dashboard/account" activeClassName="active">
                                    <div class="dashboard__sidebar--item-container">Conta</div>
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="dashboard__main">
                    <div class="dashboard__main-topbar">
                        <div class="dashboard__main-topbar__left">
                            <div class="dashboard__main-topbar--item">
                                <AccountBalance/>
                            </div>
                        </div>
                        <div class="dashboard__main-topbar__right">
                            <div class="d-inline-block align-self-center">
                                {/*<Dropdown text="Menu">
                                    <div class="dropdown-item">
                                        <Link href="/auth/logout" activeClassName="active">
                                            Logout
                                        </Link>
                                    </div>
                                </Dropdown>*/}

                                <NotificationIcon/>

                                <div class="dashboard__main-topbar--item">
                                    <Link href="/auth/logout" activeClassName="active">
                                        Logout
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="dashboard__main-content">
                        <div style="height: 100%; padding: 16px;">
                            <Match path="/dashboard/panel" include>
                                <ControlPanel/>
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

                            <Match path="/dashboard/spots" include>
                                <Spots/>
                            </Match>

                            <Match path="/dashboard/account" include>
                                <Account/>
                            </Match>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export class AccountBalance extends Component {
    constructor(props) {
        super(props);

        this.state = {
            balance: "0,00"
        };

        this.requestBalance();
    }

    requestBalance() {
        AccountManager.requestBalance().then((response) => {
            this.setState({balance: response.balance.replace('.', ',')});
        });
    }

    render({}, {balance}) {
        return (
            <span class="dashboard__account-balance">
                Saldo: R$ {balance}
                <i class="fa fa-plus" onClick={() => route('/dashboard/account/add-balance')}/>
            </span>
        )
    }

}

class NotificationIcon extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false
        }
    }

    render({}, {open}) {
        return (
            <div class="dashboard__main-topbar--item topbar-item-bell position-relative">
                <i class="fa fa-bell" aria-hidden="true" onClick={() => this.setState({open: !open})}/>
                {open && (
                    <div class="position-absolute" style="right: 0;">
                        <NotificationCard/>
                    </div>
                )}
            </div>
        )
    }
}