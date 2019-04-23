import {Component} from "preact";
import CreateAdd from "./CreateAdd";
import {LeftArrow} from "../../utils/Components";
import Match from "../../utils/Match";
import {route} from "preact-router";

export default class Ads extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Match path={"/dashboard/ads"} not>
                    <LeftArrow cb={() => route('/dashboard/ads')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/ads" exact>
                        <div class="ads-add dashboard-website__rounded-button"
                             onClick={() => route('/dashboard/ads/create')}>
                            Criar Anúncio
                        </div>
                    </Match>

                    <Match path="/dashboard/ads/create" exact>
                        <CreateAdd/>
                    </Match>
                </div>
            </div>
        )
    }
}