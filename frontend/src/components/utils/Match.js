import {Component} from "preact";
import * as UrlUtils from "./UrlUtils";

export default class Match extends Component {
    constructor(props) {
        super(props);

        this.state = {
            mode: "EXACT"
        };

        this.updateRoutingMode(props);
    }

    updateRoutingMode(props) {
        if (props.include) {
            this.setState({mode: "INCLUDE"});
        } else if (props.not) {
            this.setState({mode: "NOT"});
        }
    }

    render({children, path = ""}, {mode}) {
        switch (mode) {
            case "EXACT":
                if (UrlUtils.exact(path))
                    return children[0];
                return null;
            case "INCLUDE":
                if (UrlUtils.include(path))
                    return children[0];
                return null;
            case "NOT":
                if (UrlUtils.not(path))
                    return children[0];
                return null;
            default:
                return null;
        }
    }

}