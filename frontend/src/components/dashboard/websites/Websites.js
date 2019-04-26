import {Component} from "preact";
import {route} from "preact-router";
import Axios from 'axios';
import {HOST} from "../../../configs";
import AddWebsite from "./AddWebsite";
import {AdAxiosGet, auth} from "../../../auth";
import Match from "../../utils/Match";
import ShowWebsite from "./ShowWebsite";
import {CATEGORIES_PT} from "../../utils/WebsiteCategory";
import {LeftArrow} from "../../utils/Components";

export default class Websites extends Component {
    constructor(props) {
        super(props);

        this.state = {
            websites: []
        };
    }

    componentDidMount() {
        this.requestWebsites();
    }

    requestWebsites() {
        AdAxiosGet.get(`${HOST}/api/v1/websites`).then((response) => {
            this.setState({
                websites: response.data
            })
        });
    }

    /**
     * Callback function to reload websites
     */
    reload() {
        this.requestWebsites();
    }

    render({}, {websites}) {
        return (
            <div>
                <Match path={"/dashboard/websites"} not>
                    <LeftArrow cb={() => route('/dashboard/websites')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/websites" exact>
                        <div>
                            <div>
                                <div class="websites-add dashboard-website__rounded-button" onClick={() => route('/dashboard/websites/add')}>
                                    Adicionar seu Website
                                </div>
                            </div>
                            <div style="margin-top: 15px;">
                                {websites.map((ws) => (
                                    <Website {...ws} />
                                ))}
                            </div>
                        </div>
                    </Match>

                    <Match path="/dashboard/websites/add" exact>
                        <AddWebsite reload={this.reload.bind(this)}/>
                    </Match>

                    <Match path="/dashboard/websites/show/" include>
                        <ShowWebsite/>
                    </Match>
                </div>
            </div>
        )
    }
}

export class Website extends Component {
    constructor(props) {
        super(props);

        this.categories = CATEGORIES_PT;
    }

    displayWebsite() {
        if (this.props.id !== null)
            route(`/dashboard/websites/show/${this.props.id}`);
    }

    render({id, name, logoUrl, url, description, categories}) {
        return (
            <div class="website-item shadow" onClick={() => this.displayWebsite()}>
                <div style="display: flex;">
                    <img class="website-item__image" src={logoUrl}/>
                </div>
                <div style="margin-left: 9px; width: calc(100% - 60px - 9px);">
                    <div class="website-item__name">
                        <span>{name}</span>
                    </div>
                    <div class="website-item__description">{description}</div>
                    <div class="website-categories">
                        {categories && categories.map((cat) => (
                            <div class="dashboard-website__tag">
                                {this.categories[cat]}
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        )
    }
}