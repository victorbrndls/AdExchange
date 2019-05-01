import {Component} from "preact";
import {render as PreactRender} from "preact";

const LeftArrow = ({cb}) => (
    <div style="position: absolute;">
        <img id="websiteBackIcon" src="/assets/left-arrow.png"
             onClick={() => cb()}/>
    </div>
);

class ConfirmationModal extends Component {
    constructor(props) {
        super(props);
    }

    close() {
        document.getElementById("aeConfirmationModal").remove();
    }

    closeAndCb(){
        this.close();
        this.props.cb();
    }

    render({text, cb}) {
        return (
            <div class="ae-confirmation__modal" id="aeConfirmationModal">
                <div class="ae-confirmation__modal-content">
                    <div class="modal-header">Confirmação</div>
                    <div class="modal-body">{text}</div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onClick={this.close.bind(this)}>Fechar</button>
                        <button class="btn btn-primary" onClick={() => this.closeAndCb()}>Confirmar</button>
                    </div>
                </div>
            </div>
        )
    }

    static renderFullScreen(text, cb) {
        if (document.getElementById("aeConfirmationModal") === null)
            PreactRender(<ConfirmationModal text={text} cb={cb}/>, document.getElementsByTagName("body")[0]);
    }

}

export {
    LeftArrow,
    ConfirmationModal
}