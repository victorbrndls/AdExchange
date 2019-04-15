import {Component} from "preact";
import {Router} from 'preact-router'

import Dashboard from "./dashboard/Dashboard";
import Header from "./Header";
import Home from "./Home";

export default class App extends Component {
    constructor() {
        super();
    }

    handleRoute(e){
    }

    render() {
        return (
            <div>
                <Header/>
                <Router onChange={this.handleRoute.bind(this)}>
                    <Home path="/"/>
                    <Dashboard path="/dashboard/:*"/>
                </Router>
            </div>
        )
    }
}