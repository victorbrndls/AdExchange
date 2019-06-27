import {Component} from "preact";
import AddBalance from "./AddBalance";
import TransferBalance from "./TransferBalance";

export default class Balance extends Component{
    constructor(props){
        super(props);

        this.state = {

        };
    }

    render() {
        return (
            <div>
                <AddBalance/>
                <TransferBalance/>
            </div>
        )
    }

}