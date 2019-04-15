import {Component} from "preact";
import {Router} from 'preact-router';

import Dashboard from "./dashboard/Dashboard";
import Header from "./Header";

export default class App extends Component {
    constructor() {
        super();
    }

    handlerRoute(e) {

    }

    render() {
        return (
            <div>
                <Header/>
                <Router onChange={this.handlerRoute}>
                    <Dashboard path="/dashboard/:?"/>
                </Router>
            </div>
        )
    }
}