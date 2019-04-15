import {Component} from "preact";

export default class Header extends Component {
    constructor() {
        super();
    }

    render() {
        return (
            <h2 style="background-color: yellow;">
                Header
                <hr/>
            </h2>
        )
    }
}