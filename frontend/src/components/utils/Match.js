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
        if (props.include === true)
            this.setState({mode: "INCLUDE"});
    }

    render({children, path = ""}, {mode}) {
        switch (mode) {
            case "EXACT":
                if (Match.getCurrentPath() === path)
                    return children[0];
                return null;
            case "INCLUDE":
                if (Match.getCurrentPath().includes(path))
                    return children[0];
                return null;
            default:
                return null;
        }
    }

    static getCurrentPath(){
        return location.pathname;
    }
}
