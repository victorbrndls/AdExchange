import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../configs";
import {CATEGORIES_PT} from "../utils/WebsiteCategory";

export default class AddWebsite extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: undefined
        };

        this.categories = CATEGORIES_PT;

        this.selectedCheckBox = 0;

        this.fields = {
            name: () => document.getElementById('name'),
            url: () => document.getElementById('url'),
            logoUrl: () => document.getElementById('logoURL'),
            description: () => document.getElementById('description')
        };
    }

    componentDidMount() {
        this.resetFields();
    }

    resetFields() {
        this.fields.name().value = "";
        this.fields.url().value = "";
        this.fields.logoUrl().value = "";
        this.fields.description().value = "";

        this.selectedCheckBox = 0;
        this.getCategoriesInputs().forEach((checkbox) => {
            checkbox.checked = false;
        });
    }

    addWebsite() {
        if (!this.verifyFields())
            return;

        let formData = new FormData();
        formData.append('name', this.fields.name().value);
        formData.append('url', this.fields.url().value);
        formData.append('logoURL', this.fields.logoUrl().value);
        formData.append('description', this.fields.description().value);
        formData.append('categories', this.getCheckedCategories());

        Axios.post(`${HOST}/api/v1/websites`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }).then(() => {
            /*route('/dashboard/websites');
            this.props.reload();*/
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

    handleCheckBoxClick(e) {
        if (this.selectedCheckBox === 3) {
            if (e.target.checked)
                e.preventDefault();
        }

        if (e.target.checked) {
            this.selectedCheckBox < 3 ? this.selectedCheckBox++ : this.selectedCheckBox = 3;
        } else {
            this.selectedCheckBox > 1 ? this.selectedCheckBox-- : this.selectedCheckBox = 0;
        }
    }

    /**
     * @return {Array} Array of categories id
     */
    getCheckedCategories() {
        let ids = [];

        this.getCategoriesInputs().forEach((checkbox) => {
            ids.push(checkbox.id);
        });

        return ids;
    }

    /**
     * @return {Array}
     */
    getCategoriesInputs(){
        return Array.from(document.getElementById("addWebsiteCategories").querySelectorAll("input[type='checkbox']:checked"));
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
                        <div class="form-group websites-add__form">
                            <label>Nome</label>
                            <input id="name" class="form-control w-25 " maxLength="40"/>
                        </div>
                        <div class="form-group websites-add__form">
                            <label>URL</label>
                            <input id="url" class="form-control w-25 " placeholder="https://..."/>
                        </div>
                        <div class="form-group websites-add__form">
                            <label>URL da logo</label>
                            <input id="logoURL" class="form-control w-25" placeholder="https://..."/>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Descricao</label>
                            <textarea id="description" class="form-control w-50" maxlength="500"
                                      placeholder="Descricao" style="height: 150px;"/>
                        </div>

                        <div class="form-group">
                            <label>Categorias</label>
                            <div id="addWebsiteCategories">
                                {Object.entries(this.categories).map((category) => (
                                    <Category name={category[1]} catId={category[0]}
                                              onClickCb={this.handleCheckBoxClick.bind(this)}/>
                                ))}
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

let Category = ({name, catId, onClickCb}) => (
    <div class="custom-control custom-checkbox custom-control-inline websites-add__checkbox">
        <input class="custom-control-input" type="checkbox" value="" id={catId} onClick={onClickCb}/>
        <label class="custom-control-label" for={catId}>
            {name}
        </label>
    </div>
);