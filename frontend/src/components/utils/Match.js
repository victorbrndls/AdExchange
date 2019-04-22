import {Component} from "preact";

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
                if (getCurrentPath() === path)
                    return children[0];
                return null;
            case "INCLUDE":
                if (getCurrentPath().includes(path))
                    return children[0];
                return null;
            case "NOT":
                if (getCurrentPath() !== path)
                    return children[0];
                return null;
            default:
                return null;
        }
    }

}

export function getCurrentPath() {
    return location.pathname;
}