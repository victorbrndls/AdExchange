import {Component} from "preact";
import {getCurrentUrl} from 'preact-router';

export default class Match extends Component {
    constructor(props) {
        super(props);
    }

    render({children, path = ""}) {
        if (getCurrentUrl().includes(path))
            return children[0];

        return null;
    }
}
