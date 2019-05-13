import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost, auth} from "../../../auth";
import {CATEGORIES_PT} from "../../utils/WebsiteCategory";
import {route} from "preact-router";
import UrlUtils from "../../utils/UrlUtils";
import {ConfirmationModal} from "../../utils/Components";

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

    deleteWebsite() {
        let id = this.getIdFromUrl();

        if (id === null || id === undefined)
            return;

        ConfirmationModal.renderFullScreen("Voce tem certeza que quer deletar esse Website?", () => {
            AdAxiosPost.delete(`${HOST}/api/v1/websites/${id}`).then(() => {
                route('/dashboard/websites');
                this.props.reload();
            });
        })
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
                        <div class="dashboard-website__description">
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
                                     onClick={() => route(`/dashboard/proposals/edit?mode=NEW&websiteId=${this.getIdFromUrl()}`)}>
                                    Fazer Proposta de Anúncio
                                </div>
                            </div>
                        )}
                        {website.owner && (
                            <div style="margin-top: 15px; text-align: right;">
                                <div class="dashboard-website__rounded-button dashboard-website__edit"
                                     onClick={() => route(`/dashboard/websites/edit?id=${this.getIdFromUrl()}`)}>
                                    Editar
                                </div>
                                <div class="dashboard-website__rounded-button dashboard-website__delete"
                                     onClick={this.deleteWebsite.bind(this)}>
                                    Deletar
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        )
    }
}