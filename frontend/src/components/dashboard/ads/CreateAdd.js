import {Component} from "preact";

export default class CreateAdd extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null
        };
    }

    render({}, {error}) {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Adicionar Anúncio
                    </div>

                    <div>
                        {error && (
                            <div style="margin-top: 7px; margin-bottom: 14px;">
                                <small style="color: red;">
                                    {error}
                                </small>
                            </div>
                        )}
                    </div>

                    <div style="margin-top: 5px;">
                        <div class="form-group websites-add__form">
                            <label>Nome</label>
                            <input id="ad-name" class="form-control" maxLength="60"/>
                        </div>

                        <div class="btn"
                             style="background-color: #156dc9; color: white; font-size: 17px; font-weight: bold;">
                            Adicionar
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}