import {Component} from "preact";
import Match from "../utils/Match";
import {LeftArrow} from "../utils/Components";
import {route} from "preact-router";

export default class Proposals extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return (
            <div>
                <Match path={"/dashboard/proposals"} not>
                    <LeftArrow onClick={route.bind(this, '/dashboard/proposals')}/>
                </Match>

                <div style="width: 1000px; margin: auto;">

                </div>
            </div>
        )
    }

}