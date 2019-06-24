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
                        <div class="container ae-home-hero--container">
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
                                <div class="col-md-12 col-lg-6 text-center">
                                    <img src="https://i.imgur.com/gsB33qD.png"/>
                                </div>
                            </div>
                            <div class="row ae-home-hero-trial--container">
                                <div class="col-lg-10 col-xl-9">
                                    <div class="ae-home-hero--trial shadow container-fluid">
                                        <div class="row mb-2">
                                            <div class="col ae-home-hero--trial-header">
                                                Start your free trial
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col ae-home-hero-trial--message">
                                                Duis et mollis ipsum. Nunc fringilla ornare urna, in luctus dui
                                                suscipitt porta. Etiam convallis, ex quis efficitur blandit, felis arcu
                                                tincidunt nunc, commodo malesuada purus elit ut metus.
                                            </div>
                                            <div class="col text-right">
                                                <a class="ae-home-hero-trial--button" href="/auth?register">
                                                    Comece Agora
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-home-features">
                        <div class="container">
                            <div class="row justify-content-around mb-5">
                                <FeatureCard header="Lorem Ipsum Dolor"
                                             message="Duis et mollis ipsum. Nunc fringillta ornare urna, in luctus dui suscipit porta. Etiam convallis, ex quis efficitur blandit, felis arcu tincidunt nunc, commodo malesuada purus elit ut metus. "
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                                <FeatureCard header="Lorem Ipsum Dolor"
                                             message="Duis et mollis ipsum. Nunc fringillta ornare urna, in luctus dui suscipit porta. Etiam convallis, ex quis efficitur blandit, felis arcu tincidunt nunc, commodo malesuada purus elit ut metus. "
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                                <FeatureCard header="Lorem Ipsum Dolor"
                                             message="Duis et mollis ipsum. Nunc fringillta ornare urna, in luctus dui suscipit porta. Etiam convallis, ex quis efficitur blandit, felis arcu tincidunt nunc, commodo malesuada purus elit ut metus. "
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-home-how_to">
                        <div class="container">
                            <div class="row mb-5">
                                <div class="col ae-home-how_to--header">
                                    Como Funciona
                                </div>
                            </div>

                            <HowToCard imageSrc="https://i.imgur.com/wPuGiHs.png" header="Curabitur at ipsum elit"
                                       message="Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque interdum odio nisl, id lacinia ex ultricies at. Quisque urna ex, viverra in cursus nec, viverra ullamcorper dolor. Nullam fringilla risus eu interdum lacinia. Etiam eu venenatis ligula. "/>

                            <HowToCard imageSrc="https://i.imgur.com/wPuGiHs.png" header="Curabitur at ipsum elit"
                                       message="Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque interdum odio nisl, id lacinia ex ultricies at. Quisque urna ex, viverra in cursus nec, viverra ullamcorper dolor. Nullam fringilla risus eu interdum lacinia. Etiam eu venenatis ligula. "/>

                            <HowToCard imageSrc="https://i.imgur.com/wPuGiHs.png" header="Curabitur at ipsum elit"
                                       message="Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque interdum odio nisl, id lacinia ex ultricies at. Quisque urna ex, viverra in cursus nec, viverra ullamcorper dolor. Nullam fringilla risus eu interdum lacinia. Etiam eu venenatis ligula. "/>

                        </div>
                    </div>
                </div>
                <div>footer</div>
            </div>
        )
    }

}

const FeatureCard = ({imageSrc, header, message}) => (
    <div class="col-lg-5 ae-home-features-card">
        <div class="row mb-2">
            <div class="col-auto">
                <img class="ae-home-features-card--image" src={imageSrc}/>
            </div>
            <div class="col ae-home-features-card--header">
                {header}
            </div>
        </div>
        <div class="row">
            <div class="col ae-home-features-card--message">
                {message}
            </div>
        </div>
    </div>
);

const HowToCard = ({imageSrc, header, message}) => (
    <div class="row justify-content-around ae-home-how_to__card">
        <div class="col-md-5 col-lg-5 ae-home-how_to__card--image">
            <div class="image-bg-rotation position-relative">
                <img class="shadow position-relative" src={imageSrc}/>
            </div>
        </div>
        <div class="col-md-5 col-lg-5">
            <div class="row ae-home-how_to__card--header">
                {header}
            </div>
            <div class="row ae-home-how_to__card--message">{message}</div>
        </div>
    </div>
)