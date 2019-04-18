import {Component} from "preact";
import {Router, route} from "preact-router";
import AddWebsite from "./AddWebsite";

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
    }

    render({}, {websites}) {
        return (
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
                    <div>
                        {websites.map((ws) => (
                            <div>
                                <Website {...ws} />
                            </div>
                        ))}
                    </div>
                </div>
            </Router>
        )
    }
}

const Website = ({name, logoUrl, url, description}) => (
    <div>
        <div>
            <div>
                <img src={logoUrl}/>
            </div>
            <div>
                <div>
                    <a href={url} native>{name}</a>
                </div>
                <div>{description}</div>
            </div>
        </div>
    </div>
);