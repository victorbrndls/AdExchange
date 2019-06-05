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
                    <div class="ae-home-section ae-hero">
                        <div style="position: absolute;top: 468px; width: 100%;">
                            <img src="assets/waves.svg"/>
                        </div>
                        <div class="container ae-home">
                            <div class="row ae-home-banner">
                                <div class="col-sm-12 col-md-5">
                                    <div class="row">
                                        <h1 class="ae-home-banner__main-message">Faça seu site crescer com
                                            Adnamic</h1>
                                    </div>
                                    <div class="row">
                                        <p class="ae-home-banner-subtext">Exiba anuncios para seus cliente e pague
                                            somente pelos resultados. Exiba anuncios para seus cliente e pague somente
                                            pelos resultados. We allow you to sell custom ad spaces on-site and monetize
                                            your community through privacy-preserving and ethical ad spaces.
                                        </p>
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
                                        <img src="assets/hero-image.png"/> {/* //TODO add alt*/}
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
                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Aumente as visitas à loja"
                                                         subHeader="Receba mais clientes na loja com anúncios que ajudam as pessoas a encontrar sua empresa no mapa."
                                                         imgSrc="/assets/small-business.png"/>
                                        </div>

                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Aumente o número de visitas ao site"
                                                         subHeader="Aumente o número de vendas on-line, reservas ou inscrições na lista de e-mails com anúncios na Internet que direcionam as pessoas para seu site."
                                                         imgSrc="/assets/small-web.png"/>
                                        </div>

                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Receba mais chamadas"
                                                         subHeader="Receba mais chamadas de clientes com anúncios que incluem seu número e um botão Clique para ligar."
                                                         imgSrc="/assets/small-money-bag.png"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-features">
                        <div class="container ae-home">
                            <div class="row flex-column align-items-center">
                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col">
                                            <img class="ae-features-img" src="https://picsum.photos/450/300"/>
                                        </div>
                                        <div class="col">
                                            <h3 class="font-weight-bold">Monetized in minutes</h3>
                                            <p class="ae-features-text">Setting up ad spaces is incredibly
                                                quick. It only takes a few easy steps
                                                to display and sell instantly bookable ad spaces and sponsorship spots
                                                on your site.
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col">
                                            <h3 class="font-weight-bold">No network - you are in control</h3>
                                            <p class="ae-features-text">Your ads are not being served
                                                through a network, but purchased manually through your site. No tracking
                                                needed. You decide what ads are going to show up.
                                            </p>
                                        </div>
                                        <div class="col">
                                            <img class="ae-features-img" src="https://picsum.photos/451/300"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col">
                                            <img class="ae-features-img" src="https://picsum.photos/449/300"/>
                                        </div>
                                        <div class="col">
                                            <h3 class="font-weight-bold">Monetized in minutes</h3>
                                            <p class="ae-features-text">Setting up ad spaces is incredibly
                                                quick. It only takes a few easy steps
                                                to display and sell instantly bookable ad spaces and sponsorship spots
                                                on your site.
                                            </p>
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
                                                             "1 Website",
                                                             "3 Spots",
                                                             "4 Anuncios",
                                                             "Editor de anuncio simples"
                                                         ]}/>
                                        </div>
                                        <div class="col-sm-12 col-md-4">
                                            <PricingCard title="Profissional" price="13"
                                                         benefits={[
                                                             "Websites ilimitados",
                                                             "Anuncios ilimitados",
                                                             "Spots ilimitados",
                                                             "Editor de anuncio PRO",
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
            <p class="text-black-50">R$&nbsp;
                <span class="ae-home-pricing__price">{price}</span>
                /mês
            </p>
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