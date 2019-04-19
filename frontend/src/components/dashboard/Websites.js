import {Component} from "preact";
import {Router, route} from "preact-router";
import Axios from 'axios';
import {HOST} from "../../configs";
import AddWebsite from "./AddWebsite";
import {auth} from "../../auth";
import Match from "../utils/Match";
import ShowWebsite from "./ShowWebsite";

export default class Websites extends Component {
    constructor(props) {
        super(props);

        this.state = {
            websites: []
        }
    }

    componentDidMount() {
        this.requestWebsites();
    }

    requestWebsites() {
        Axios.get(`${HOST}/api/v1/websites`, {
            params: {
                token: auth.getToken()
            }
        }).then((response) => {
            this.setState({
                websites: response.data
            })
        });
    }

    render({}, {websites}) {
        return (
            <div style="width: 1000px; margin: auto;">
                <Match path="/dashboard/websites" exact>
                    <div>
                        <div>
                            <div class="websites-add" onClick={() => route('/dashboard/websites/add')}>
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
                    <AddWebsite/>
                </Match>

                <Match path="/dashboard/websites/show/" include>
                    <ShowWebsite/>
                </Match>
            </div>
        )
    }
}

class Website extends Component {
    constructor(props) {
        super(props);
    }

    displayWebsite() {
        if (this.props.id !== null)
            route(`/dashboard/websites/show/${this.props.id}`);
    }

    render({id, logoUrl, url, description}) {
        return (
            <div class="website-item shadow" onClick={() => this.displayWebsite()}>
                <div style="display: flex;">
                    <img class="website-item__image" src={logoUrl}/>
                </div>
                <div style="margin-left: 9px; width: calc(100% - 60px - 9px);">
                    <div class="website-item__name">
                        <a href={url} target="_blank" native>{url}</a>
                    </div>
                    <div class="website-item__description">{description}</div>
                </div>
            </div>
        )
    }
}