import {Component} from "preact";
import ControlPanelManager from "../../../managers/ControlPanelManager";

export default class NotificationCard extends Component {

    constructor(props) {
        super(props);

        this.state = {
            notifications: []
        };
    }

    componentDidMount() {
        ControlPanelManager.getNotifications().then((notifs) => {
            this.setState({notifications: notifs.reverse()});
        });
    }

    render({}, {notifications}) {
        return (
            <div>
                <div
                    class="card card-notification">
                    <div class="card-header dashboard-panel__notification-card">
                        <span class="dashboard-panel__notification-header">Notificações</span>
                    </div>
                    <div class="dashboard-panel__notifications card-body">
                        {notifications.map((notif) => (
                            <div>
                                <NotificationItem {...notif}/>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        )
    }
}

class NotificationItem extends Component {
    notificationMapper = {
        'NEW_PROPOSAL': NewProposalNotification,
        'REJECTED_PROPOSAL': RejectedProposalNotification,
        'REVIEWED_PROPOSAL': ReviewedProposalNotification,
        'ACCEPTED_PROPOSAL': AcceptedProposalNotification
    };

    constructor(props) {
        super(props);
    }

    render(props) {
        let type = props.type;
        let NotificationTypeConstructor = this.notificationMapper[type];

        if (!NotificationTypeConstructor)
            return;

        return (
            <div class="dashboard-panel__notification--item">
                <i class={`fa ${NotificationTypeConstructor._iconClass} notification-icon`}/>
                <span>{NotificationTypeConstructor._messageConvertor({...props})}</span>
            </div>
        )
    }
}

class NotificationType {
    constructor(iconClass, converter) {
        this._iconClass = iconClass;
        this._messageConvertor = converter || (() => "Erro ao mostrar notificacao");
    }
}

let NewProposalNotification = new NotificationType('fa-envelope',
    ({senderName = 'Alguem', websiteName}) => (
        <span>{senderName} enviou uma proposta para <i>{websiteName}</i></span>));

let RejectedProposalNotification = new NotificationType('fa-minus',
    ({senderName = 'Alguem', websiteName}) => (
        <span>{senderName} rejeitou a proposta para <i>{websiteName}</i></span>));

let ReviewedProposalNotification = new NotificationType('fa-repeat',
    ({senderName = 'Alguem', websiteName}) => (
        <span>{senderName} revisou a proposta para <i>{websiteName}</i> e enviou-la novamente</span>));

let AcceptedProposalNotification = new NotificationType('fa-check',
    ({websiteName}) => (<span>A proposta para <i>{websiteName}</i> foi aceita</span>));
