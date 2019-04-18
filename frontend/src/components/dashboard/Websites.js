import {Component} from "preact";
import {Router, route} from "preact-router";
import Axios from 'axios';
import {HOST} from "../../configs";
import AddWebsite from "./AddWebsite";
import {auth} from "../../auth";

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
                <Router>
                    <AddWebsite path="/dashboard/websites/add"/>
                    <div path="/dashboard/websites">
                        <div>
                            <div class="websites-add" onClick={() => {
                                route('/dashboard/websites/add')
                            }}>
                                Adicionar seu Website
                            </div>
                        </div>
                        <div style="margin-top: 15px;">
                            {websites.map((ws) => (
                                <div>
                                    <Website {...ws} />
                                </div>
                            ))}
                        </div>
                    </div>
                </Router>
            </div>
        )
    }
}

const Website = ({name, logoUrl, url, description}) => (
    <div class="website-item shadow">
        <div style="display: flex;">
            <img class="website-item__image" src={logoUrl}/>
        </div>
        <div style="margin-left: 9px; width: calc(100% - 60px - 9px);">
            <div class="website-item__name">
                <a href={url} native>{url}</a>
            </div>
            <div class="website-item__description">{description}</div>
        </div>
    </div>
);