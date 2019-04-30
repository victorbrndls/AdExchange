import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../../configs";
import {AdAxiosGet, auth} from "../../../auth";
import {CATEGORIES_PT} from "../../utils/WebsiteCategory";
import {route} from "preact-router";
import UrlUtils from "../../utils/UrlUtils";

export default class ShowWebsite extends Component {
    constructor(props) {
        super(props);

        this.categories = CATEGORIES_PT;

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

        AdAxiosGet.get(`${HOST}/api/v1/websites/${id}`, {
            params: {
                token: auth.getToken()
            }
        }).then((response) => {
            this.setState({website: response.data});
        });

    }

    getIdFromUrl() {
        return UrlUtils.getCurrentPath().split("show/")[1];
    }

    render({}, {website}) {
        if (website === null)
            return;

        return (
            <div class="dashboard-website shadow">
                <div class="dashboard-website__up">
                    <div class="dashboard-website__up-logo">
                        <img src={website.logoUrl}/>
                    </div>
                    <div class="dashboard-website__up-url">
                        <a href={website.url} native target="_blank">{website.name}</a>
                    </div>
                </div>
                <div class="dashboard-website__down">
                    <div style="margin: 0 49px;">
                        <div style="text-align: center; margin: 40px 0;">
                            {website.description}
                        </div>
                        <div>
                            {website.categories && website.categories.map((cat) => (
                                <div class="dashboard-website__tag">
                                    {this.categories[cat]}
                                </div>
                            ))}
                        </div>
                        {!website.owner && (
                            <div style="margin-top: 15px;">
                                <div class="dashboard-website__rounded-button dashboard-website__create-proposal"
                                     onClick={() => route(`/dashboard/proposals/edit/new?websiteId=${this.getIdFromUrl()}`)}>
                                    Fazer Proposta de Anúncio
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        )
    }
}