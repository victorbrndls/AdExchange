import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../configs";

export default class AddWebsite extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: undefined
        };

        this.props.url = () => document.getElementById('url');
        this.props.logoUrl = () => document.getElementById('logoURL');
        this.props.description = () => document.getElementById('description');
    }

    addWebsite() {
        if (!this.verifyFields())
            return;

        let formData = new FormData();
        formData.append('url', this.props.url().value);
        formData.append('logoURL', this.props.logoUrl().value);
        formData.append('description', this.props.description().value);

        Axios.post(`${HOST}/api/v1/websites`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then((response) => {
            console.log(response);
        });
    }

    verifyFields() {
        this.setState({error: undefined});

        if (this.props.url().value.length < 5) {
            this.setState({error: "URL invalido"});
            return false;
        }

        if (this.props.description().value.length < 25) {
            this.setState({error: "Por favor descreva seu website melhor (pelo menos 25 caracteres)"});
            return false;
        }

        return true;
    }

    render({}, {error}) {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Adicionar Website
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
                        <div class="form-group">
                            <label>URL*</label>
                            <input id="url" class="form-control w-25 " placeholder="https://..."/>
                        </div>
                        <div class="form-group">
                            <label>URL da logo</label>
                            <input id="logoURL" class="form-control w-25 " placeholder="https://..."/>
                        </div>

                        <div class="form-group">
                            <label>Descricao*</label>
                            <textarea id="description" class="form-control w-50" maxlength="150"
                                      placeholder="Descricao"/>
                        </div>

                        <div class="form-group">
                            <label>Categorias</label>
                            <div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" value="" id="checkbox-1"/>
                                    <label class="form-check-label" for="checkbox-1">
                                        Educacao
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" value="" id="defaultCheck1"/>
                                    <label class="form-check-label" for="defaultCheck1">
                                        Desenvolvimento Pessoal
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" value="" id="defaultCheck1"/>
                                    <label class="form-check-label" for="defaultCheck1">
                                        Inovacao
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="btn"
                             style="background-color: #156dc9; color: white; font-size: 17px; font-weight: bold;"
                             onClick={this.addWebsite.bind(this)}>
                            Adicionar
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}