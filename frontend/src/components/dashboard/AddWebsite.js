import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../configs";
import {route} from "preact-router";

export default class AddWebsite extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: undefined
        };

        this.fields = {
            name: () => document.getElementById('name'),
            url: () => document.getElementById('url'),
            logoUrl: () => document.getElementById('logoURL'),
            description: () => document.getElementById('description')
        };
    }

    componentDidMount() {
        this.fields.name().value = "";
        this.fields.url().value = "";
        this.fields.logoUrl().value = "";
        this.fields.description().value = "";
    }

    addWebsite() {
        if (!this.verifyFields())
            return;

        let formData = new FormData();
        formData.append('name', this.fields.name().value);
        formData.append('url', this.fields.url().value);
        formData.append('logoURL', this.fields.logoUrl().value);
        formData.append('description', this.fields.description().value);

        Axios.post(`${HOST}/api/v1/websites`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then((response) => {
            route('/dashboard/websites');
        });
    }

    verifyFields() {
        this.setState({error: undefined});

        if (this.fields.name().value.length < 2) {
            this.setState({error: "Nome muito pequeno"});
            return false;
        }

        if (this.fields.url().value.length < 5) {
            this.setState({error: "URL invalido"});
            return false;
        }

        if (this.fields.description().value.length < 25) {
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
                            <label>Nome</label>
                            <input id="name" class="form-control w-25 " maxLength="30"/>
                        </div>
                        <div class="form-group">
                            <label>URL</label>
                            <input id="url" class="form-control w-25 " placeholder="https://..."/>
                        </div>
                        <div class="form-group">
                            <label>URL da logo</label>
                            <input id="logoURL" class="form-control w-25" placeholder="https://..."/>
                        </div>

                        <div class="form-group">
                            <label>Descricao</label>
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