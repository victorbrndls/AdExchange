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

    render({cb}) {
        return (
            <div class="ae-confirmation__modal" id="aeConfirmationModal">
                <div class="ae-confirmation__modal-content">
                    <div class="modal-header">Header</div>
                    <div class="modal-body">body</div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onClick={this.close.bind(this)}>Fechar</button>
                        <button class="btn btn-primary">Confirmar</button>
                    </div>
                </div>
            </div>
        )
    }

    static renderFullScreen() {
        if (document.getElementById("aeConfirmationModal") === null)
            PreactRender(<ConfirmationModal/>, document.getElementsByTagName("body")[0]);
    }

}

export {
    LeftArrow,
    ConfirmationModal
}