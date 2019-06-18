import {Component} from "preact";
import {route} from "preact-router";
import {HOST} from "../../../configs";
import EditWebsite from "./EditWebsite";
import {AdAxiosGet, auth} from "../../../auth";
import Match from "../../utils/Match";
import ShowWebsite from "./ShowWebsite";
import {CATEGORIES_PT} from "../../utils/WebsiteCategory";
import {LeftArrow} from "../../utils/Components";
import anime from "animejs";
import WebsiteManager from "../../../managers/WebsiteManager";

export default class Websites extends Component {
    constructor(props) {
        super(props);

        this.state = {
            websites: []
        };

        this.methods = {
            // Function implemented by WebsiteFilters that returns the selected filters
            getFilters: () => [],
            closeFilterDropdown: () => {
            }
        }
    }

    componentDidMount() {
        this.requestWebsites();
    }

    requestWebsites() {
        WebsiteManager.getWebsites().then((data) => {
            this.setState({
                websites: data
            })
        });
    }

    requestWebsitesWithFilter() {
        this.methods.closeFilterDropdown();

        let filters = this.methods.getFilters();

        WebsiteManager.getWebsiteWithCategories(filters).then((data) => {
            this.setState({
                websites: data
            })
        });
    }

    /**
     * Callback function to reload websites
     */
    reload() {
        this.requestWebsites();
    }

    render({}, {websites}) {
        return (
            <div>
                <Match path={"/dashboard/websites"} not>
                    <LeftArrow cb={() => route('/dashboard/websites')}/>
                </Match>

                <div class="dashboard__main-content-container">
                    <Match path="/dashboard/websites" exact>
                        <div>
                            <div>
                                <div class="websites-add dashboard-website__rounded-button mb-3"
                                     onClick={() => route('/dashboard/websites/edit?type=new')}>
                                    Adicionar seu Website
                                </div>

                                <div class="d-flex">
                                    <WebsiteFilter methods={this.methods}/>
                                    <div class="website-filter dashboard-website__rounded-button mb-3"
                                         onClick={() => this.requestWebsitesWithFilter()}>
                                        Filtrar
                                    </div>
                                </div>
                            </div>

                            <div>
                                {websites.map((ws) => (
                                    <Website {...ws} />
                                ))}

                                {websites.length === 0 && (
                                    <div class="proposal__none">Nenhum no momento</div>
                                )}
                            </div>
                        </div>
                    </Match>

                    <Match path="/dashboard/websites/edit" include>
                        <EditWebsite reload={this.reload.bind(this)}/>
                    </Match>

                    <Match path="/dashboard/websites/show/" include>
                        <ShowWebsite reload={this.reload.bind(this)}/>
                    </Match>
                </div>
            </div>
        )
    }
}

export class Website extends Component {
    constructor(props) {
        super(props);

        this.categories = CATEGORIES_PT;
    }

    displayWebsite() {
        if (this.props.id !== null)
            route(`/dashboard/websites/show/${this.props.id}`);
    }

    render({id, name, logoUrl, url, description, categories}) {
        return (
            <div class="website-item shadow" onClick={() => this.displayWebsite()}>
                <div style="display: flex;">
                    <img class="website-item__image" src={logoUrl}/>
                </div>
                <div style="margin-left: 9px; width: calc(100% - 60px - 9px);">
                    <div class="website-item__name">
                        <span>{name}</span>
                    </div>
                    <div class="website-item__description">{description}</div>
                    <div class="website-categories">
                        {categories && categories.map((cat) => (
                            <div class="dashboard-website__tag">
                                {this.categories[cat]}
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        )
    }
}

class WebsiteFilter extends Component {
    constructor(props) {
        super(props);

        this.CATEGORIES = CATEGORIES_PT;

        this.state = {
            open: false,
            filters: {
                ...Object.keys(this.CATEGORIES).reduce((acc, current) => {
                    acc[current] = true;
                    return acc;
                }, {})
            }
        };

        props.methods.getFilters = () => {
            return Object.keys(this.state.filters).reduce((acc, current) => {
                if (this.state.filters[current])
                    acc.push(current);

                return acc;
            }, []);
        };

        props.methods.closeFilterDropdown = () => this.handleOpenClick(false);
    }

    handleFilterItemClick(item) {
        this.setState({filters: {...this.state.filters, [item]: !this.state.filters[item]}});
    }

    // Animate dropdown
    handleOpenClick(open) {
        if (open) {
            this.setState({open}, () => {
                anime({
                    targets: '.websites-filter-dropdown',
                    height: document.getElementsByClassName('websites-filter-dropdown--list')[0].clientHeight || 892,
                    duration: 700,
                    easing: 'easeInOutCubic',
                });
            });
        } else {
            anime({
                targets: '.websites-filter-dropdown',
                height: 0,
                duration: 700,
                easing: 'easeInOutCubic',
            }).finished.then(() => this.setState({open}));
        }
    }

    /**
     *
     * @param select {Boolean} If TRUE selects all filters, if FALSE selects none.
     */
    updateFilters(select) {
        let filters = this.state.filters;

        Object.keys(filters).forEach((key) => {
            filters[key] = select;
        });

        this.setState({filters: filters});
    }

    render({}, {open, filters}) {
        return (
            <div class="position-relative">
                <div class={`websites-filter-container shadow-sm ${open ? 'filter--active' : ''}`}
                     onClick={() => this.handleOpenClick(!open)}>
                    Escolher Filtro
                    <i class="fa fa-caret-down ml-2" aria-hidden="true"/>
                </div>
                {open && (
                    <div class="websites-filter-dropdown shadow-sm">
                        <ul class="list-unstyled websites-filter-dropdown--list">
                            <li class="websites-filter__command" onClick={() => this.updateFilters(true)}>
                                Selecionar Todos
                            </li>
                            <li class="websites-filter__command" onClick={() => this.updateFilters(false)}>
                                Remover Filtro
                            </li>

                            {Object.entries(this.CATEGORIES).map((entry) => (
                                <li class={`websites-filter--item ${filters[entry[0]] ? 'filter--active' : ''}`}
                                    onClick={() => this.handleFilterItemClick(entry[0])}>
                                    <div class="blocking-container" style={'left: 0'}/>
                                    {entry[1]}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        )
    }
}