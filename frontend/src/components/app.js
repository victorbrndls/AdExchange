import {Component} from "preact";
import {Router} from 'preact-router'

import Dashboard from "./dashboard/Dashboard";
import Header from "./Header";
import Home from "./Home";
import Auth from "./Auth";

export default class App extends Component {
    constructor(props) {
        super(props);

        //TODO log user here
    }

    handleRoute(e) {
    }

    render() {
        return (
            <div>
                <Header/>
                <Router onChange={this.handleRoute.bind(this)}>
                    <Home path="/"/>
                    <Dashboard path="/dashboard/:*"/>
                    <Auth path="/auth/:*"/>
                </Router>
            </div>
        )
    }
}

// TODO class={`console ${isConsoleOpen ? '' : 'is-minimized'}`} - Can use `