import {Component} from "preact";

export default class ControlPanel extends Component {
    constructor(props) {
        super(props);
    }

    render(){
        return (
            <div>
                <div class="col-sm-12 col-md-7 col-lg-4">
                <div class="card">
                    <div class="card-header dashboard-panel__notification-card">
                        <span class="dashboard-panel__notification-header">Notificações</span>
                    </div>
                    <div class="dashboard-panel__notifications card-body">
                        <div class="dashboard-panel__notification--item">
                            <i class="fa fa-envelope notification-icon notification-icon__sent"/>
                            <span>"Nome" enviou uma proposta para "Website"</span>
                        </div>
                        <div class="dashboard-panel__notification--item">
                            <i class="fa fa-check notification-icon notification-icon__accepted"/>
                            <span>A sua proposta para "Website" foi aceita</span>
                        </div>
                        <div class="dashboard-panel__notification--item">
                            <i class="fa fa-repeat notification-icon notification-icon__repeat"/>
                            <span>"Nome" viu sua proposta e enviou-la novamente</span>
                        </div>
                        <div class="dashboard-panel__notification--item">
                            <i class="fa fa-minus notification-icon notification-icon__rejected"/>
                            <span>Sua proposta para "Website" foi rejeitada</span>
                        </div>
                    </div>
                </div>
                </div>

                {/*Remove this latter*/}
                <div style="position: absolute;bottom: 15px;right: 22px;">
                    <h2>Develop only the MVP and just that</h2>

                    <div>How can I develop this feature better or in a different way?<br/></div>
                    <div>Design Pattern? / Different Architecture?<br/></div>
                    <div>What is the simplest thing that could possibly work?<br/></div>
                    <br/>

                    Imagens, gráficos, vídeos, listas, links, gifs, infográficos
                </div>
            </div>
        )
    }
}