import {Component} from "preact";
import {Link} from "preact-router";
import Header from "./Header";

export default class WebsiteOwnerHome extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div style="background-color: #f9fafc;">
                <div>
                    <Header view='WEBSITE'/>
                </div>

                <div>
                    <div class="ae-home-section ae-hero">
                        <div class="ae-hero__wave">
                            <img src="assets/waves.svg"/>
                        </div>
                        <div class="container ae-home">
                            <div class="row ae-home-banner">
                                <div class="col-sm-12 col-lg-5 ae-hero-text">
                                    <div class="row">
                                        <h1 class="ae-home-banner__main-message">
                                            Atraia novos clientes para sua empresa
                                        </h1>
                                    </div>
                                    <div class="row">
                                        <p class="ae-home-banner-subtext">
                                            Utilize o Adnamic para criar anúncios que fazem sua empresa vender
                                            mais.
                                            Tenha <b>controle</b> de seus anúncios escolhendo onde, por quanto tempo e
                                            por qual valor serão exibidos.
                                        </p>
                                    </div>
                                    <div class="row ae-home-banner-wrapper">
                                        <Link class="p-3 my-3 ae-home-banner-button" href="/auth?register">
                                            Comece agora
                                            <img src="assets/right-arrow.svg"/>
                                        </Link>
                                    </div>
                                </div>
                                <div class="col-sm-0 col-md-1 ae-hero-spacer"/>
                                <div class="col-sm-12 col-lg-6">
                                    <div class="row">
                                        <div class="col">
                                            <img style="width:100%;" src="assets/hero-image.png"/> {/* //TODO add alt*/}
                                        </div>
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
                                        <h2>Porque escolher <b>Adnamic</b></h2>
                                    </div>
                                    <div class="row justify-content-center">
                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Controle"
                                                         subHeader="No Adnamic você escolhe em qual site mostrar seus anúncios, por quanto tempo e qual o modo de pagamento."
                                                         imgSrc="/assets/small-control.png"/>
                                        </div>

                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Análise"
                                                         subHeader="Veja dados de seus anúncios como quantidade de cliques e visualizações."
                                                         imgSrc="/assets/small-graph.png"/>
                                        </div>

                                        <div class="col-sm-12 col-md-6 col-lg-4 ae-home-benefits-wrapper">
                                            <BenefitCard header="Prático"
                                                         subHeader="Nossa plataforma conta com diversos recursos para criar seus anúncios de um jeito simples e rápido."
                                                         imgSrc="/assets/small-exercise.png"/>
                                        </div>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-features">
                        <div class="container ae-home">
                            <div class="row flex-column align-items-center">
                                <div class="row justify-content-center ae-home-benefits-header mb-5">
                                    <h2>Como funciona</h2>
                                </div>

                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col-sm-12 col-md ae-features-image-wrapper text-center">
                                            <img class="ae-features-img" src="assets/data.svg"/>
                                        </div>
                                        <div class="col">
                                            <h3 class="ae-features-title">Crie seu anúncio</h3>
                                            <p class="ae-features-text">
                                                Por causa de nossa grande coleção de modelos, você não presisa ser um
                                                designer para fazer um bom anúncio, basta escolher um modelo e
                                                prencher os dados. Simples e prático.
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col">
                                            <h3 class="ae-features-title">Faça uma proposta</h3>
                                            <p class="ae-features-text">
                                                Utilize nossos filtros para selecionar qual o site correto para exibir
                                                seu anúncio e envie uma proposta com o anúncio que deseja usar, o modo
                                                de pagamento e o valor que está disposto a pagar.
                                            </p>
                                        </div>
                                        <div class="col-sm-12 col-md ae-features-image-wrapper text-center">
                                            <img class="ae-features-img" src="assets/proposal.svg"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-12 col-lg-11 ae-features-row">
                                    <div class="row">
                                        <div class="col-sm-12 col-md ae-features-image-wrapper text-center">
                                            <img class="ae-features-img" src="assets/cloud.svg"/>
                                        </div>
                                        <div class="col">
                                            <h3 class="ae-features-title">Seu anúncio está funcionando</h3>
                                            <p class="ae-features-text">
                                                Caso o dono do site aceite sua proposta, o seu anúncio já estará
                                                disponível para visualização.
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
                                        <div class="col-sm-9 col-md-6 col-lg-5 col-xl-4">
                                            <PricingCard title="Básico" price="0"
                                                         benefits={[
                                                             "1 Website",
                                                             "3 Spots",
                                                             "4 Anuncios",
                                                             "Editor de anuncio simples"
                                                         ]}/>
                                        </div>
                                        <div class="col-sm-9 col-md-6 col-lg-5 col-xl-4">
                                            <PricingCard title="Profissional" price="13"
                                                         benefits={[
                                                             "Websites ilimitados",
                                                             "Anúncios ilimitados",
                                                             "Spots ilimitados",
                                                             "Editor de anuncio PRO",
                                                         ]}/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ae-home-section ae-website-owner">
                        <div class="container ae-home">
                            <div class="row">
                                <div class="col">

                                </div>
                            </div>
                        </div>
                    </div>

                    {/*<div class="ae-home-section ae-cta">
                        <div class="container ae-home">
                            <div class="row">
                                <div class="col">

                                </div>
                            </div>
                        </div>
                    </div>*/}

                    <div class="ae-home-section ae-footer shadow-lg">
                        <div class="container ae-home py-3">
                            <div class="row">
                                <div class="col-xs-12 col-md-auto ae-logo ae-footer-logo">
                                    <img src="/assets/logo.png"/>
                                </div>

                                <div class="col-sm-12 col-md ae-footer">
                                    <div class="row align-items-center h-100 ae-footer-info">
                                        <a native class="col-auto" href="mailto:tidderjail2@gmail.com">Contato</a>
                                        <Link class="col-auto" href="/policy">Política de Privacidade</Link>
                                    </div>
                                </div>

                                <div class="col-sm-12 col-md align-self-center ae-footer-copyright">
                                    <span class="text-black-50">
                                        © 2019 Adnamic. Todos os direitos reservados
                                    </span>
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
            <img src={imgSrc}/>
        </div>
        <p class="tile-title">{header}</p>
        <p class="tile-body">{subHeader}</p>
    </div>
);