import {Component} from "preact";

export default class AddWebsite extends Component {
    constructor(props) {
        super(props);

        this.props.domain = () => document.getElementById('');
        this.props.logoUrl = () => document.getElementById('');
        this.props.description = () => document.getElementById('');
        this.props.tags = () => document.getElementById('');
    }

    render() {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="margin-bottom: 20px; font-family: Raleway; font-size: 30px;">
                        Adicionar Website
                    </div>
                    <div>
                        <div class="form-group">
                            <label>Dominio*</label>
                            <input class="form-control w-25 " placeholder="https://..."/>
                        </div>
                        <div class="form-group">
                            <label>URL da logo</label>
                            <input class="form-control w-25 " placeholder="https://..."/>
                        </div>

                        <div class="form-group">
                            <label>Descricao*</label>
                            <textarea class="form-control w-50" placeholder="Descricao"/>
                        </div>

                        <div class="form-group">
                            <label>Categorias</label>
                            <div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" value="" id="defaultCheck1"/>
                                    <label class="form-check-label" for="defaultCheck1">
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

                        <div class="btn" style="background-color: #156dc9; color: white; font-size: 17px;">
                            Adicionar
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}