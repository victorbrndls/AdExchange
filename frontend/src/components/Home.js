import {Component} from "preact";
import {Link} from "preact-router";

export default class Home extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div style="background-color: #f9fafc;">
                <div class="header shadow-sm">
                    <div class="container">
                        <div id="logo">
                            <img src="/assets/logo.png"/>
                        </div>
                        <div class="ae-navbar">
                            <div class="ae-navbar--link">
                                <Link href="/auth?register">Criar Conta</Link>
                            </div>
                            <div class="ae-navbar--link">
                                <Link href="/auth?login">Entrar</Link>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <div class="ae-home-section">
                        <div class="container ae-home">
                            <div class="row ae-home-banner">
                                <div class="col-sm-12 col-md-5">
                                    <div class="row">
                                        <h1 class="ae-home-banner__main-message">Faça seu site crescer com
                                            Adnamic</h1>
                                    </div>
                                    <div class="row">
                                        <p>Exiba anuncios para seus cliente e pague somente pelos resultados. Exiba
                                            anuncios
                                            para seus cliente e pague somente pelos resultados. Soapbox is the only
                                            tool you
                                            need to record, edit, and share videos in minutes. </p>
                                    </div>
                                    <div class="row">
                                        <a class="p-3 my-3 ae-home-banner-button trans-0_3s">
                                            <span>Comece agora</span>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-sm-0 col-md-1"/>
                                <div class="col-sm-12 col-md-6">
                                    <div class="row">
                                        <img src="https://via.placeholder.com/570x350.png"/> {/* //TODO add alt*/}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-benefits">
                        <div class="container ae-home">
                            <div class="row">
                                <div class="col">
                                    <div class="row justify-content-center ae-home-benefits-header mb-5">
                                        <h2>Alcance os resultados desejados</h2>
                                    </div>
                                    <div class="row justify-content-center">
                                        <div class="col-sm-12 col-md-6 col-lg-3">
                                            <BenefitCard header="Aumente o número de visitas ao site"
                                                         subHeader="Aumente o número de vendas on-line, reservas ou inscrições na lista de e-mails com anúncios na Internet que direcionam as pessoas para seu site."
                                                         imgSrc="https://via.placeholder.com/120x100.png"/>
                                        </div>
                                        <div class="ae-home-tile col-sm-12 col-md-6 col-lg-3">
                                            <BenefitCard header="Receba mais chamadas"
                                                         subHeader="Receba mais chamadas de clientes com anúncios que incluem seu número e um botão Clique para ligar."
                                                         imgSrc="https://via.placeholder.com/120x100.png"/>
                                        </div>
                                        <div class="ae-home-tile col-sm-12 col-md-6 col-lg-3">
                                            <BenefitCard header="Aumente as visitas à loja"
                                                         subHeader="Receba mais clientes na loja com anúncios que ajudam as pessoas a encontrar sua empresa no mapa."
                                                         imgSrc="https://via.placeholder.com/120x100.png"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-pricing">
                        <div class="container ae-home">
                            <div class="row">
                                <div class="col">
                                    <div class="row justify-content-center ae-home-benefits-header my-4">
                                        <h2 class="ae-pricing__header">Preços</h2>
                                    </div>
                                    <div class="row justify-content-center mb-4">
                                        <div class="col-sm-12 col-md-4">
                                            <PricingCard title="Básico" price="0"
                                                         benefits={[
                                                             "There are many variations of Lorem",
                                                             "There are many variations of Lorem",
                                                             "There are many variations of Lorem",
                                                             "There are many variations of Lorem"
                                                         ]}/>
                                        </div>
                                        <div class="col-sm-12 col-md-4">
                                            <PricingCard title="Pro" price="13"
                                                         benefits={[
                                                             "Quickly build an effective pricing",
                                                             "Quickly build an effective pricing",
                                                             "Quickly build an effective pricing",
                                                             "Quickly build an effective pricing",
                                                             "Quickly build an effective pricing"
                                                         ]}/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

const PricingCard = ({title, price, benefits = []}) => (
    <div class="ae-home-pricing shadow mb-4">
        <h3>{title}</h3>
        <div>
            <p class="text-black-50">$&nbsp;
                <span class="ae-home-pricing__price">{price}</span>
                /mês</p>
        </div>
        <ul class="ae-home-pricing-benefits">
            {benefits.map((benefit) => (
                <li>{benefit}</li>
            ))}
        </ul>
        <a class="ae-home-pricing__button">
            <span>Comece agora</span>
        </a>
    </div>
);

const BenefitCard = ({header, subHeader, imgSrc}) => (
    <div class="ae-home-tile">
        <div class="tile-header">
            <img src={imgSrc}/> {/*Icon should be SVG*/}
        </div>
        <p class="tile-title">{header}</p>
        <p class="tile-body">{subHeader}</p>
    </div>
);