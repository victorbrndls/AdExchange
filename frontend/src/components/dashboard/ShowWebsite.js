import {Component} from "preact";
import {getCurrentPath} from "../utils/Match"
import Axios from 'axios';
import {HOST} from "../../configs";
import {auth} from "../../auth";

export default class ShowWebsite extends Component {
    constructor(props) {
        super(props);

        this.state = {
            website: null
        }
    }

    componentDidMount() {
        this.requestWebsite();
    }

    requestWebsite() {
        let id = this.getIdFromUrl();

        if (id === null || id === undefined)
            return;

        Axios.get(`${HOST}/api/v1/websites/${id}`, {
            params: {
                token: auth.getToken()
            }
        }).then((response) => {
            this.setState({website: response.data});
        });

    }

    getIdFromUrl() {
        return getCurrentPath().split("show/")[1];
    }

    render({}, {website}) {
        if (website === null)
            return;

        return (
            <div class="dashboard-website shadow">
                <div class="dashboard-website__up">
                    <div class="dashboard-website__up-logo">
                        <img src="https://startuptracker.io/cdn/startups/6f/6FoZv0i0kJ4pNWZ2oSe5whyxZmZot8Ly74HbmqxtCL8.png"/>
                    </div>
                    <div class="dashboard-website__up-url">
                        <a href={website.url} native>{website.name}</a>
                    </div>
                </div>
                <div class="dashboard-website__down">
                    <div style="margin: 0 49px;">
                        <div style="text-align: center; margin: 40px 0;">
                            ${website.description}
                        </div>
                        <div>
                            <div class="dashboard-website__tag">
                                Health
                            </div>
                            <div class="dashboard-website__tag">
                                Learning
                            </div>
                            <div class="dashboard-website__tag">
                                Web
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}