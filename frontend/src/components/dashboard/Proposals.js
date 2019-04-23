import {Component} from "preact";
import Match from "../utils/Match";
import {LeftArrow} from "../utils/Components";
import {route} from "preact-router";
import AddProposal from "./EditProposal";

export default class Proposals extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return (
            <div>
                <Match path={"/dashboard/proposals"} not>
                    <LeftArrow cb={() => route('/dashboard/proposals')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/proposals/edit" include>
                        <AddProposal/>
                    </Match>
                </div>
            </div>
        )
    }

}