import {Component} from "preact";
import {Router} from 'preact-router'
import asyncComponent from "./utils/AsyncComponent";

import Home from "./home/AdvertiserHome";
import Auth from "./Auth";

const Dashboard = asyncComponent(() =>
    import('./dashboard/Dashboard').then(module => module.default)
);

export default class App extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="h-100">
                <Router>
                    <Home path="/"/>
                    <Dashboard path="/dashboard/:*"/>
                    <Auth path="/auth/:*"/>
                </Router>
            </div>
        )
    }
}