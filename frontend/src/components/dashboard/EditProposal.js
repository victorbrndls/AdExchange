import {Component} from "preact";
import {Website} from "./Websites";
import Axios from "axios";
import {HOST} from "../../configs";
import {auth} from "../../auth";

export default class AddProposal extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
            mode: "EDIT", // EDIT or NEW
            proposalId: parseInt(Math.random() * 1000),
            website: {}
        };

        this.updateMode();

        this.requestWebsiteInformation();
    }

    updateMode() {
        if (location.pathname.includes("/edit/new"))
            this.setState({mode: "NEW"});
    }

    getWebsiteId() {
        switch (this.state.mode) {
            case "EDIT":
                return 0;
            case "NEW":
                let query = new URLSearchParams(location.search);
                return query.get('websiteId') || -1;
            default:
                return -1;
        }
    }

    requestWebsiteInformation() {
        let id = this.getWebsiteId();

        if (id === -1) {
            this.setState({error: "websiteId invalido"});
            return;
        }

        Axios.get(`${HOST}/api/v1/websites/${id}`, {
            params: {
                token: auth.getToken()
            }
        }).then((response) => {
            this.setState({website: response.data});
        });
    }

    render({}, {website, proposalId, error}) {
        return (
            <div>
                <div style="font-family: Raleway; font-size: 30px;">
                    Proposta #{proposalId}
                </div>
                <div>
                    <div>
                        WebsiteId: {this.getWebsiteId()}
                    </div>

                    <div style="position: relative;">
                        <div class="blocking-container"/>
                        <Website {...website}/>
                    </div>
                </div>
            </div>
        )
    }
}