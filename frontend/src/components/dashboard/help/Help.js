import {Component} from "preact";

export default class Help extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="container">
                <div class="row mb-3">
                    <div class="col">
                        <div class="help-header">
                            Suporte
                        </div>
                    </div>
                </div>

                {/* Website owner roadmap*/}

                <div class="row">
                    <div class="col">
                        <HelpItem title={"Dashboard"}>
                            <span class="help-subitem-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Architecto autem delectus
                                ducimus eos ex fuga fugit hic illum ipsa ipsum iste, laboriosam molestiae nisi nulla
                                odio perferendis provident totam vel?
                            </span>
                        </HelpItem>

                        <HelpItem title={"Websites"}>
                            <dt>Tenho um website</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Na página ‘Websites’ você pode adicionar/editar o seu site ao nossa lista de sites
                                cadastrados.
                                Construa um boa descrição, descreva qual tipo de conteúdo você posta, qual seu público
                                alvo e o número de visualizações mensais. Será ela que convencerá outras pessoas a
                                anunciarem em
                                seu website.
                            </dd>

                            <dt>Quero anunciar em um website</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Na página ‘Websites’ você encontra sites para fazer seus anúncios, leia a descrição e
                                veja se o público alvo do site é parecido com o seu, utilize os filtros para facilitar a
                                procura. Quando você encontrar o site em que deseja anunciar clique no botão “Fazer
                                Proposta de Anúncio” para enviar uma proposta ao dono do site. (Para informações sobre
                                como fazer um proposta leia o item “Proposta” abaixo)
                            </dd>
                        </HelpItem>

                        <HelpItem title={"Propostas"}>
                            <span class="help-subitem-text">
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab, accusantium animi autem
                            dolorum ea eaque eligendi eos esse, eveniet explicabo, iusto modi officiis porro
                            praesentium quam rerum vel velit veritatis! Lorem ipsum dolor sit amet, consectetur
                            adipisicing elit. Ab, accusantium animi autem
                            dolorum ea eaque eligendi eos esse, eveniet explicabo, iusto modi officiis porro
                            praesentium quam rerum vel velit veritatis!
                            </span>
                        </HelpItem>

                    </div>
                </div>
            </div>
        )
    }
}

const HelpItem = ({title, children}) => (
    <dl class="help-item--container">
        <dt class="help-item--container--header">{title}</dt>
        <dd class="help-item--container--body">
            {children}
        </dd>
    </dl>
);