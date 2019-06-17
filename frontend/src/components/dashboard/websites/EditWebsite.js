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
            this.selectedCheckBox = this.state.website.categories.length || 0;
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
                        <div class="row">
                            <div class="col-lg-6 col-xl-4">
                                <div class="form-group websites-add__form">
                                    <label>Nome</label>
                                    <input id="name" class="form-control" placeholder="Nome do Website" maxLength="40"
                                           value={website.name}
                                           onChange={(e) => {
                                               this.setState({website: {...website, name: e.target.value}})
                                           }}/>
                                    <small class="form-text ad-error">
                                        {error.name}
                                    </small>
                                </div>
                            </div>

                            <div class="col-lg-6 col-xl-4">
                                <div class="form-group websites-add__form">
                                    <label>URL do Website</label>
                                    <input id="url" class="form-control  " placeholder="https://..." value={website.url}
                                           onChange={(e) => {
                                               this.setState({website: {...website, url: e.target.value}})
                                           }}/>
                                    <small class="form-text ad-error">
                                        {error.url}
                                    </small>
                                </div>
                            </div>

                            <div class="col-lg-6 col-xl-4">
                                <div class="form-group websites-add__form">
                                    <label>Impressões mensais</label>
                                    <abbr
                                        title="O número de visualizações mensais em seu site">
                                        <i class="fa fa-question fa-abbr-icon" aria-hidden="true"/>
                                    </abbr>
                                    <input id="logoURL" class="form-control" placeholder="1500"
                                           value={website.monthlyImpressions} onChange={(e) => {
                                        this.setState({website: {...website, monthlyImpressions: e.target.value}})
                                    }}/>
                                </div>
                            </div>

                            <div class="col-lg-8 col-xl-6 d-flex">
                                <div class="form-group websites-add__form flex-grow-1">
                                    <label>URL da logo</label>
                                    <input id="logoURL" class="form-control " placeholder="https://..."
                                           value={website.logoUrl} onChange={(e) => {
                                        this.setState({website: {...website, logoUrl: e.target.value}})
                                    }}/>
                                    <small class="form-text text-muted">Use uma imagem quadrada preferencialmente
                                    </small>
                                </div>
                                <div class="ml-3">
                                    <img class="websites-add--logo" src={website.logoUrl || "/assets/placeholder.png"}/>
                                </div>
                            </div>


                        </div>

                        <div class="row">
                            <div class="col">
                                <div class="form-group websites-add__form">
                                    <label>Descrição</label>
                                    <textarea id="description" class="form-control" maxLength="1500"
                                              placeholder="Descreva qual tipo de conteúdo você posta, qual seu público alvo, etc...."
                                              style="height: 250px;" value={website.description}
                                              onChange={(e) => {
                                                  this.setState({website: {...website, description: e.target.value}})
                                              }}/>
                                    <small class="form-text ad-error">
                                        {error.description}
                                    </small>
                                </div>
                            </div>
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

                        <div class="btn dashboard-add__button mb-4"
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