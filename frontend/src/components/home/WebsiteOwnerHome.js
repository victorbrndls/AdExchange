import {Component} from "preact";
import Header from "./Header";
import Footer from "./Footer";

export default class WebsiteOwnerHome extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div style={{backgroundColor: '#F9F8FD'}}>
                <div>
                    <Header view='WEBSITE'/>
                </div>

                <div>
                    <div class="ae-home-section ae-home-hero">
                        <div class="container ae-home-hero--container">
                            <div class="row">
                                <div class="col-md-12 col-lg-6 ae-home-hero--left">
                                    <div class="row mb-3 row">
                                        <div class="col ae-home-hero__text ae-home-hero__main-text">
                                            Ganhe dinheiro com seu Website
                                        </div>
                                    </div>
                                    <div class="row ml-1 justify-content-md-center">
                                        <div class="col ae-home-hero__text ae-home-hero__secondary-text">
                                            <ul class="list-unstyled ae-home-hero--benefits-list">
                                                <li class="list-diamond">Simples e rápido</li>
                                                <li class="list-diamond">Não precisa entender de programação</li>
                                                <li class="list-diamond">Exiba seus próprios anúncios</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-12 col-lg-6 text-center" style={{height: 230}}>
                                    <img src="/assets/website-owner-home.svg" class="ae-home-hero--image"/>
                                </div>
                            </div>
                            <div class="row ae-home-hero-trial--container">
                                <div class="col-lg-10 col-xl-9">
                                    <div class="ae-home-hero--trial shadow container-fluid">
                                        <div class="row mb-2">
                                            <div class="col ae-home-hero--trial-header">
                                                Crie valor para seus visitantes
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-12 col-lg-8 ae-home-hero-trial--message">
                                                Exiba anúncios que são relevantes ao seu público aumentando ainda mais o valor gerado a eles.
                                            </div>
                                            <div class="col-12 col-lg-4 ae-home-hero-trial--button-container">
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
                                <FeatureCard header="Nós cuidamos de tudo"
                                             message="O Adnamic cuida de toda a parte complicada para você, basta utilizar nossa plataforma intuitiva para adicionar anúncios ao seu website.   "
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                                <FeatureCard header="Monitore o desempenho"
                                             message="Monitore o desempenho dos anúncios que aparecem em seu website, veja quantas pessoas estão clicando e visualizando eles."
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                                <FeatureCard header="Controle em 1º lugar"
                                             message="Em nossa plataforma você escolhe quais anúncios aparecerão em seu website."
                                             imageSrc="https://img.icons8.com/color/344/lock-2.png"/>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-home-how_to">
                        <div class="container">
                            <div class="row mb-5">
                                <div class="col ae-home--section-header">
                                    Como Funciona
                                </div>
                            </div>

                            <HowToCard imageSrc="https://i.imgur.com/KAkFQap.png" header="Adicione seu Website"
                                       message="Para que outras pessoas conseguam achar seu Website é necessário cadastrá-lo em nossa plataforma."/>
                            <HowToCard imageSrc="https://i.imgur.com/pPeU4kW.png" header="Receba Propostas"
                                       message="Após ter cadastrado seu Website, as pessoas que se interessarem em anunciar em seu Website enviaram propostas para você."/>
                            <HowToCard imageSrc="https://i.imgur.com/jXppkKZ.png" header="Exiba o Anúncio"
                                       message="Caso você aceite a proposta, só falta exibir o anúncio em seu Website."/>

                        </div>
                    </div>

                    <div class="ae-home-section ae-home-prices">
                        <div class="container">
                            <div class="row mb-5">
                                <div class="col ae-home--section-header">
                                    Preços
                                </div>
                            </div>

                            <div class="row justify-content-center">
                                <div class="col-8 col-sm-7 col-md-5 col-lg-4 shadow ae-home-prices-card">
                                    <div class="ae-home-prices-card--header mb-1">
                                        Starter
                                    </div>
                                    <div class="ae-home-prices-card--price mb-3">
                                        <span>R$</span>
                                        <span class="ae-home-prices-card--price--number">0</span>
                                        <span>/mês</span>
                                    </div>

                                    <div class="mb-5">
                                        <ul class="list-unstyled ae-home-prices-card--features">
                                            <li>Website ilimitados</li>
                                            <li>Anúncios ilimitados</li>
                                            <li>Contratos ilimitados</li>
                                            <li>Spots ilimitados</li>
                                            <li>Taxa de transação baixa</li>
                                        </ul>
                                    </div>

                                    <div class="mb-2">
                                        <a class="ae-home-prices-card--cta" href="/auth?register">
                                            Comece Agora
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <Footer/>
                </div>
            </div>
        )
    }

}

const FeatureCard = ({imageSrc, header, message}) => (
    <div class="col-12 col-lg-5 ">
        <div class="ae-home-features-card">
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
    </div>
);

const HowToCard = ({imageSrc, header, message}) => (
    <div class="row justify-content-around ae-home-how_to__card">
        <div class="col-md-5 col-lg-5 ae-home-how_to__card--image">
            <div class="image-bg-rotation position-relative">
                <img class="shadow position-relative" src={imageSrc}/>
            </div>
        </div>
        <div class="col-md-5 col-lg-5 ae-home-how_to__card--text">
            <div class="row">
                <div class="col ae-home-how_to__card--header">
                    {header}
                </div>
            </div>
            <div class="row">
                <div class="col ae-home-how_to__card--message">
                    {message}
                </div>
            </div>
        </div>
    </div>
)