import {Component} from "preact";
import {Link} from "preact-router";

export default class Home extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div style="background-color: #f9fafc;">
                <div class="header">
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

                <div class="container ae-home">
                    <section>
                        <div class="row ae-home-banner">
                            <div class="col-sm-12 col-md-5">
                                <div class="row">
                                    <h1 class="ae-home-banner__main-message">Faça seu site crescer com Adnamic</h1>
                                </div>
                                <div class="row">
                                    <p>Exiba anuncios para seus cliente e pague somente pelos resultados</p>
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
                    </section>
                    <section>
                        <div class="row">
                            <div class="col">
                                <div class="row justify-content-center ae-home-benefits-header mb-5">
                                    <h2>Alcance os resultados desejados</h2>
                                </div>
                                <div class="row">
                                    <div class="ae-home-tile col-sm-12 col-md-6 col-lg-4">
                                        <div class="tile-header">
                                            {/*Icon should be SVG*/}
                                        </div>
                                        <p class="tile-title">Aumente o número de visitas ao site</p>
                                        <p class="tile-body">Aumente o número de vendas on-line, reservas ou inscrições
                                            na lista de
                                            e-mails com anúncios na Internet que direcionam as pessoas para seu
                                            site.</p>
                                    </div>
                                    <div class="ae-home-tile col-sm-12 col-md-6 col-lg-4">
                                        <div class="tile-header">
                                            {/*Icon should be SVG*/}
                                        </div>
                                        <p class="tile-title">Aumente as visitas à loja</p>
                                        <p class="tile-body">Receba mais clientes na loja com anúncios que ajudam as
                                            pessoas a encontrar sua empresa no mapa</p>
                                    </div>
                                    <div class="ae-home-tile col-sm-12 col-md-6 col-lg-4">
                                        <div class="tile-header">
                                            {/*Icon should be SVG*/}
                                        </div>
                                        <p class="tile-title">Aumente o número de visitas ao site</p>
                                        <p class="tile-body">Aumente o número de vendas on-line, reservas ou inscrições
                                            na lista de
                                            e-mails com anúncios na Internet que direcionam as pessoas para seu
                                            site.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        )
    }
}