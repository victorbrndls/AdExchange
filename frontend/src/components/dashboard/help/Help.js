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
                            <span class="help-subitem-text">
                                Na página “Dashboard” você pode ver informações de desempenho de seus anúncios como:
                                <ul>
                                    <li>Número de visualizações diárias;</li>
                                    <li>Número de cliques diários;</li>
                                </ul>
                            </span>
                        </HelpItem>

                        <HelpItem title={"Websites"}>
                            <dt>Para proprietários de website</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Na página ‘Websites’ você pode adicionar e editar o seu site ao nossa lista de
                                sites cadastrados. Construa um boa descrição, descreva qual tipo de conteúdo você posta,
                                qual seu público alvo e o número de visualizações mensais. Será ela que convencerá
                                outras pessoas a anunciarem em seu website.
                            </dd>

                            <dt>Para quem quer anunciar em nossos sites afiliados</dt>
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
                                Quando uma pessoa deseja anunciar em um site, ela precisa enviar um proposta para o dono
                                do site com informações como qual anuncio quer usar, por quanto tempo quer anunciar,
                                qual o modo de pagamento e o valor de pagamento.
                            </span>

                            <dt>Como fazer uma proposta</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Na página ‘Websites’, procure o site em que deseja anunciar. Depois clique nele e por
                                fim clique no botão “Fazer Proposta de Anúncio”.
                            </dd>

                            <span class="help-subitem-text">
                                Quando o dono do website receber a proposta ela poderá escolher entre:

                                <ul>
                                    <li>
                                        Rejeitar a proposta clicando no botão “Rejeitar proposta”.
                                    </li>
                                    <li>
                                        Modificar a proposta e enviá-la para quem a criou, que por sua vez irá analisar e enviá-la novamente ao dono do site caso aceite os novos valores.
                                    </li>
                                    <li>
                                        Aceitar a proposta. Quando a proposta for aceita pelo dono do site ela se torna
                                        um contrato e estará disponível na página ‘Contratos’.
                                    </li>
                                </ul>
                            </span>
                        </HelpItem>

                        <HelpItem title={"Contratos"}>
                            <span class="help-subitem-text">
                                Um contrato é um acordo entre uma pessoa que deseja anunciar e um dono de website. Após
                                ser criado um contrato não pode ser modificado nem deletado. No contrato há informações
                                sobre qual anuncio será utilizado, sua validade, o método e valor de pagamento.
                            </span>

                            <dt>Como criar um contrato</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Quando uma proposta é aceita por um dono de website, ela se torna um contrato. (Leia o
                                item "Propostas" para mais informações)
                            </dd>
                        </HelpItem>

                        <HelpItem title={"Anúncios"}>
                            <span class="help-subitem-text">
                                Na página “Anúncios” você pode visualizar seus anúncios existentes ou criar novos.
                            </span>

                            <dt>Como criar um anúncio</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Dentro da página ‘Anúncios’ clique no botão “Criar Anúncio”. Na tela que aparecerá você
                                deve preencher informações como nome do anúncio e escolher qual modelo de anúncio que
                                usar, após isso clique no botão “Criar”.
                            </dd>
                        </HelpItem>

                        <HelpItem title={"Spots"}>
                            <dt>Como criar um Spot</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                O Spot é um objeto que referencia um anúncio que você deseja exibir e um anúncio reserva
                                caso exista. Cada Spot tem um ID único que é usado para referenciá-lo em seu site. Para
                                exibir um anúncio em seu site, entre na página ‘Spots’ e clique no botão “Criar Spot”.
                                Após isso selecione o contrato do anúncio que deseja exibir e um anúncio reserva que
                                aparecerá caso ocorra um erro ao mostrar o anúncio principal, depois clique em “Criar”.
                                Copie o <span style="color:red;">script¹</span> abaixo para algum lugar dentro da página
                                onde você deseja exibir seu
                                Spot. Volte para a página ‘Spots’ e nela aparecerá o Spot que foi criado. Agora copie o
                                <span style="color:blue;"> script²</span> onde você quer exibir seu anúncio. Copie o
                                ‘Id’ do Spot que você deseja
                                mostrar e substitua o campo {"{id-do-spot}"} no <span
                                style="color:blue;"> script²</span> que você colou em seu site.
                            </dd>

                            <dt style="color:red;">Script¹</dt>
                            <dd>
                                <div class="help-subitem-text">
                                    Esse script só precisa ser colocado 1 vez por página.
                                </div>
                                <code class="ml-3">
                                    {"<script src=\"http://localhost:8081/AdExchange.js\" async></script>"}
                                </code>
                            </dd>

                            <dt style="color:blue;">Script²</dt>
                            <dd>
                                <div class="help-subitem-text">
                                    Coloque esse script quantas vezes você quiser por página, cada script mostrará o
                                    anúncio referenciado pelo Spot.
                                </div>
                                <code class="ml-3">
                                    {"<div data-ae-id=\"{id-do-spot}\"></div>"}
                                </code>
                            </dd>

                            <dt>Como exibir um anúncio próprio</dt>
                            <dd class="help-subitem-margin help-subitem-text">
                                Para exibir um anúncio que você mesmo criou em seu site, entre na página ‘Spots’ e
                                clique no botão
                                “Criar Spot”. Após isso deixe o campo "Contrato" vazio e selecione o anúncio que deseja
                                exibir como anúncio reserva, depois clique em “Criar”.
                            </dd>
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