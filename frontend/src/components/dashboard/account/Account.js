import {Component} from "preact";
import Match from "../../utils/Match";
import {LeftArrow} from "../../utils/Components";
import {route} from "preact-router";

export default class Account extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Match path={"/dashboard/account"} not>
                    <LeftArrow cb={() => route('/dashboard/account')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/account" exact>
                        <div>
                            A
                        </div>
                    </Match>
                </div>
            </div>
        )
    }
}