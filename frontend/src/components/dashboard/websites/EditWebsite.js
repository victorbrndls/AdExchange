import {Component} from "preact";
import Axios from 'axios';
import {HOST} from "../../../configs";
import {CATEGORIES_PT} from "../../utils/WebsiteCategory";
import {route} from "preact-router";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";

export default class EditWebsite extends Component {
    constructor(props) {
        super(props);

        this.categories = CATEGORIES_PT;
        this.selectedCheckBox = 0;

        this.state = {
            mode: 'NEW',
            error: {},
            website: {
                id: null,
                name: "",
                url: "",
                logoUrl: "",
                description: "",
                categories: []
            }
        };

        this.updateMode();
    }

    updateMode() {
        if (location.search.includes('id')) {
            this.setState({mode: 'EDIT'});
        }

        if (this.state.mode === 'EDIT') {
            let id = new URLSearchParams(location.search).get('id');
            this.setState({website: {...this.state.website, id: id}});

            this.requestWebsiteInformation();
        }
    }

    requestWebsiteInformation() {
        let id = this.state.website.id;

        if (id === "" || id === null || id === undefined)
            return;

        AdAxiosGet.get(`${HOST}/api/v1/websites/${id}`).then((response) => {
            this.setState({website: response.data});
        });
    }

    addWebsite() {
        if (!this.verifyFields())
            return;

        let wb = this.state.website;

        let formData = new FormData();
        formData.append('id', wb.id);
        formData.append('name', wb.name);
        formData.append('url', wb.url);
        formData.append('logoURL', wb.logoUrl);
        formData.append('description', wb.description);
        formData.append('categories', wb.categories);

        AdAxiosPost.post(`${HOST}/api/v1/websites`, formData).then(() => {
            route('/dashboard/websites');
            this.props.reload();
        });
    }

    verifyFields() {
        this.setState({error: {}});

        if (this.state.website.name.length < 2) {
            this.setState({
                error: {
                    ...this.state.error,
                    name: "O nome do website deve conter pelo menos 2 caracteres"
                }
            });
            return false;
        }

        if (this.state.website.url.length < 5) {
            this.setState({error: {...this.state.error, url: "O url do website não é válido"}});
            return false;
        }

        if (this.state.website.description.length < 25) {
            this.setState({
                error: {
                    ...this.state.error,
                    description: "Por favor forneça uma descrição melhor de seu website"
                }
            });
            return false;
        }

        if (this.selectedCheckBox < 1) {
            this.setState({error: {...this.state.error, categories: "Por favor selecione pelo menos 1 categoria"}});
            return false;
        }

        return true;
    }

    handleCheckBoxClick(e) {
        if (this.selectedCheckBox === 3) {
            if (e.target.checked) {
                e.preventDefault();
                return;
            }
        }

        let cats = this.state.website.categories;

        if (e.target.checked) {
            cats.push(e.target.id); // Add the category
            this.selectedCheckBox < 3 ? this.selectedCheckBox++ : this.selectedCheckBox = 3;
        } else {
            cats = cats.filter(el => el !== e.target.id); // Remove the category
            this.selectedCheckBox > 1 ? this.selectedCheckBox-- : this.selectedCheckBox = 0;
        }

        this.setState({website: {...this.state.website, categories: cats}});
    }

    render({}, {error, website}) {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Adicionar Website
                    </div>

                    <div style="margin-top: 5px;">
                        <div class="form-group websites-add__form">
                            <label>Nome</label>
                            <input id="name" class="form-control w-25 " maxLength="40" value={website.name}
                                   onChange={(e) => {
                                       this.setState({website: {...website, name: e.target.value}})
                                   }}/>
                            <small class="form-text ad-error">
                                {error.name}
                            </small>
                        </div>
                        <div class="form-group websites-add__form">
                            <label>URL</label>
                            <input id="url" class="form-control w-25 " placeholder="https://..." value={website.url}
                                   onChange={(e) => {
                                       this.setState({website: {...website, url: e.target.value}})
                                   }}/>
                            <small class="form-text ad-error">
                                {error.url}
                            </small>
                        </div>
                        <div class="form-group websites-add__form">
                            <label>URL da logo</label>
                            <input id="logoURL" class="form-control w-25" placeholder="https://..."
                                   value={website.logoUrl} onChange={(e) => {
                                this.setState({website: {...website, logoUrl: e.target.value}})
                            }}/>
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Descrição</label>
                            <textarea id="description" class="form-control" maxLength="1500"
                                      placeholder="Descrição" style="height: 250px;" value={website.description}
                                      onChange={(e) => {
                                          this.setState({website: {...website, description: e.target.value}})
                                      }}/>
                            <small class="form-text ad-error">
                                {error.description}
                            </small>
                        </div>

                        <div class="form-group">
                            <label>Categorias
                                <small> (máx 3)</small>
                            </label>
                            <div id="addWebsiteCategories">
                                {Object.entries(this.categories).map((category) => (
                                    <Category name={category[1]} catId={category[0]}
                                              onClickCb={this.handleCheckBoxClick.bind(this)}
                                              checked={website.categories.includes(category[0])}/>
                                ))}
                            </div>
                            <small class="form-text ad-error">
                                {error.categories}
                            </small>
                        </div>

                        <div class="btn dashboard-add__button"
                             onClick={this.addWebsite.bind(this)}>
                            Adicionar
                        </div>

                    </div>
                </div>
            </div>
        )
    }
}

let Category = ({name, catId, onClickCb, checked}) => (
    <div class="custom-control custom-checkbox custom-control-inline websites-add__checkbox">
        <input class="custom-control-input" type="checkbox" value="" id={catId} onClick={onClickCb} checked={checked}/>
        <label class="custom-control-label" for={catId}>
            {name}
        </label>
    </div>
);