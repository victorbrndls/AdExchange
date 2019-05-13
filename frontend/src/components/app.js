import {Component} from "preact";
import {Router} from 'preact-router'

import Dashboard from "./dashboard/Dashboard";
import Home from "./Home";
import Auth from "./Auth";

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