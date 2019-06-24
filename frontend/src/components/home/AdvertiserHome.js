import {Component} from "preact";
import Header from "./Header";

export default class AdvertiserHome extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <div>
                    <Header/>
                </div>

                <div>
                    <div class="ae-home-section ae-home-hero">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12 col-lg-6">
                                    <div class="row ae-home-hero__text ae-home-hero__main-text mb-3">
                                        The Holy Grail Of Productivity
                                    </div>
                                    <div class="row ae-home-hero__text ae-home-hero__secondary-text ml-1">
                                        <ul class="list-unstyled">
                                            
                                            <li class="list-diamond">Organize your projects, your way</li>
                                            <li class="list-diamond">Track all your work in one place</li>
                                            <li class="list-diamond">Collaborate more effectively</li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-md-12 col-lg-6">
                                    <img src="https://i.imgur.com/gsB33qD.png"/>
                                </div>
                            </div>
                            <div class="row">

                            </div>
                        </div>
                    </div>
                </div>
                <div>footer</div>
            </div>
        )
    }

}