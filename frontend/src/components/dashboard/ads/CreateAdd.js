import {Component} from "preact";
import "../../../styles/ae.css";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import AdCarousel from "./AdCarousel";
import {route} from "preact-router";
import anime from "animejs";

const DEFAULT_AD_BY_TYPE = {
    'TEXT': AdCarousel.TextAds[0],
    'IMAGE': AdCarousel.ImageAds[0]
};

export default class CreateAdd extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            mode: 'NEW',
            ad: {
                type: 'TEXT',
                ...AdCarousel.TextAds[0]
            }
        };

        this.showTemplates = false;

        this.updateMode();
        this.requestAdInformation();
    }

    updateMode() {
        let params = new URLSearchParams(location.search);

        if (params.get('mode') === 'edit')
            this.setState({mode: 'EDIT'});
    }

    requestAdInformation() {
        if (this.state.mode === 'NEW')
            return;

        let id = new URLSearchParams(location.search).get('id');

        if (id !== null) {
            this.adId = id;

            AdAxiosGet.get(`${HOST}/api/v1/ads/${id}?embed=parsedOutput`).then((response) => {
                this.setState({ad: response.data});
            });
        }
    }

    handleAdTypeChange(type) {
        if (this.state.mode !== 'EDIT') // Don't change the type if the mode is EDIT
            this.setState({ad: {type: type, ...DEFAULT_AD_BY_TYPE[type]}});
    }

    setAdTemplate(ad, type) {
        this.setState({ad: {type: type, ...ad}});
    }

    handleTextChange(e) {
        this.setState({ad: {...this.state.ad, text: e.target.value}});

        this.parseTextInput(this.state.ad.text);
    }

    handleShowTemplatesChange(open) {
        anime({
            targets: '.ads-more-templates-container',
            maxHeight: open ? 300 : 0,
            duration: 700,
            easing: 'easeInOutCubic',
        }).finished.then(() => this.showTemplates = open);
    }

    parseTextInput(input) {
        let formData = new FormData();
        formData.append("input", input);

        AdAxiosPost.post(`${HOST}/api/v1/ads/parser`, formData).then((response) => {
            this.setState({ad: {...this.state.ad, parsedOutput: response.data}});
        });
    }

    submitAd() {
        let formData = this.createRequestFormData();

        let reqMode = this.state.mode === 'EDIT' ? 'put' : 'post'; // PUT or POST
        let endpoint = `${HOST}/api/v1/ads${reqMode === 'put' ? `/${this.state.ad.id}` : ''}`;

        AdAxiosPost[reqMode](endpoint, formData).then(() => {
            route('/dashboard/ads');
            this.props.reload();
        }).catch((error) => {
            this.handleErrorResponse(error);
        });
    }

    handleErrorResponse(errorResponse) {
        this.setState({error: {}});

        let error = this.state.error;

        switch (errorResponse.response.data) {
            case 'INVALID_AD_NAME':
                this.setState({error: {...error, adName: "Nome do anúncio inválido."}});
                return;
            case 'INVALID_AD_REF_URL':
                this.setState({error: {...error, adRefUrl: "URL alvo do anúncio inválido."}});
                return;
            case 'INVALID_AD_TEXT':
                this.setState({error: {...error, adText: "Texto do anúncio inválido."}});
                return;
            case 'INVALID_AD_TEXT_ALIGNMENT':
                this.setState({error: {...error, adTextAlignment: "Alinhamento do texto inválido."}});
                return;
            case 'INVALID_AD_TEXT_SIZE':
                this.setState({error: {...error, adTextSize: "Tamanho da fonte inválido."}});
                return;
            case 'INVALID_AD_BG_COLOR':
                this.setState({error: {...error, adBgColor: "Cor de fundo inválida."}});
                return;
            case 'INVALID_AD_TEXT_COLOR':
                this.setState({error: {...error, adTextColor: "Cor do texto inválida."}});
                return;
            case 'INVALID_AD_IMAGE_URL':
                this.setState({error: {...error, adImageUrl: "URL da imagem inválido."}});
                return;
        }
    }

    createRequestFormData() {
        let ad = this.state.ad;

        let formData = new FormData();
        formData.append('name', ad.name || "");
        formData.append('type', ad.type);
        formData.append('refUrl', ad.refUrl || "");

        switch (ad.type) {
            case 'TEXT':
                formData.append('text', ad.text);
                formData.append('textAlignment', ad.textAlignment);
                formData.append('textSize', ad.textSize);
                formData.append('bgColor', ad.bgColor);
                formData.append('textColor', ad.textColor);
                break;
            case 'IMAGE':
                formData.append('imageUrl', ad.imageUrl);
                break;
        }

        return formData;
    }

    render({}, {error, mode, ad}) {
        let edit_m = mode === 'EDIT';

        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Criar Anúncio
                    </div>

                    <div style="margin-top: 5px;">
                        <div class="form-group websites-add__form">
                            <label>Nome</label>
                            <input id="ad-name" class="form-control" value={ad.name} maxLength="60"
                                   onChange={(e) => this.setState({ad: {...ad, name: e.target.value}})}/>
                            {error.adName && (
                                <small class="form-text ad-error">
                                    {error.adName}
                                </small>)}
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Modelo do Anúncio</label>

                            <div class="btn-group btn-group-toggle d-block" data-toggle="buttons">
                                <label
                                    class={`create-add__btn-border ${ad.type === 'TEXT' ? 'create--add__btn-border--active ' : ''}
                                     ${edit_m && ad.type !== 'TEXT' ? 'create-add__btn-border--disabled' : ''}`}
                                    onClick={() => this.handleAdTypeChange('TEXT')}>
                                    Texto
                                </label>
                                <label
                                    class={`create-add__btn-border ${ad.type === 'IMAGE' ? 'create--add__btn-border--active ' : ''}
                                     ${edit_m && ad.type !== 'IMAGE' ? 'create-add__btn-border--disabled' : ''}`}
                                    onClick={() => this.handleAdTypeChange('IMAGE')}>
                                    Imagem
                                </label>
                            </div>

                            <div class="d-flex justify-content-center">
                                <div class="m-4">
                                    <div class="shadow ads-ad-wrapper">
                                        {ad.type === 'TEXT' && (
                                            <TextAd {...ad}/>
                                        )}

                                        {ad.type === 'IMAGE' && (
                                            <ImageAd {...ad}/>
                                        )}
                                    </div>
                                </div>
                            </div>

                            <div>
                                <div class="text-center">
                                    <label class="ae-label ads-more-templates"
                                           onClick={() => this.handleShowTemplatesChange(!this.showTemplates)}>
                                        Ver outros modelos
                                    </label>
                                </div>

                                <div class="ads-more-templates-container">

                                    {ad.type === 'TEXT' && AdCarousel.TextAds.map((ad) => (
                                        <div class="m-4">
                                            <div class="shadow ads-ad-wrapper"
                                                 onClick={() => this.setAdTemplate(ad, 'TEXT')}>
                                                <TextAd {...ad}/>
                                            </div>
                                        </div>
                                    ))}

                                    {ad.type === 'IMAGE' && AdCarousel.ImageAds.map((ad) => (
                                        <div class="m-4">
                                            <div class="shadow ads-ad-wrapper"
                                                 onClick={() => this.setAdTemplate(ad, 'IMAGE')}>
                                                <ImageAd {...ad}/>
                                            </div>
                                        </div>
                                    ))}

                                </div>
                            </div>
                        </div>

                        {ad.type === 'TEXT' && (
                            <div>
                                <div class="form-group websites-add__form">
                                    <label>Texto</label>
                                    <textarea id="createAdTextArea" class="form-control" value={ad.text}
                                              onChange={this.handleTextChange.bind(this)}/>
                                    <div>
                                        <small>Opções para mudar o texto</small>
                                        <br/>
                                        <small class="ml-3">__palavras em itálico__ => <i>palavras em itálico</i>
                                        </small>
                                        <br/>
                                        <small class="ml-3">**frase em negrito** => <b>frase em negrito</b></small>
                                        <br/>
                                        <small class="ml-3">\\ => Quebra de linha</small>
                                    </div>

                                    <small class="form-text ad-error">
                                        {error.adText}
                                    </small>
                                </div>

                                <div class="row">
                                    <div class="col-auto">
                                        <div class="form-group websites-add__form">
                                            <label>Formatação do texto</label>
                                            <div class="btn-group btn-group-toggle d-block" data-toggle="buttons">
                                                <label
                                                    class={`create-add__btn-border ${ad.textAlignment === 'LEFT' ? 'create--add__btn-border--active' : ''}`}
                                                    onClick={() => this.setState({ad: {...ad, textAlignment: 'LEFT'}})}>
                                                    <i class="fa fa-align-left" aria-hidden="true"/>
                                                </label>
                                                <label
                                                    class={`create-add__btn-border ${ad.textAlignment === 'CENTER' ? 'create--add__btn-border--active' : ''}`}
                                                    onClick={() => this.setState({
                                                        ad: {
                                                            ...ad,
                                                            textAlignment: 'CENTER'
                                                        }
                                                    })}>
                                                    <i class="fa fa-align-center" aria-hidden="true"/>
                                                </label>
                                                <label
                                                    class={`create-add__btn-border ${ad.textAlignment === 'RIGHT' ? 'create--add__btn-border--active' : ''}`}
                                                    onClick={() => this.setState({
                                                        ad: {
                                                            ...ad,
                                                            textAlignment: 'RIGHT'
                                                        }
                                                    })}>
                                                    <i class="fa fa-align-right" aria-hidden="true"/>
                                                </label>
                                                <small class="form-text ad-error">
                                                    {error.adTextAlignment}
                                                </small>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-auto">
                                        <div class="form-group websites-add__form">
                                            <label>Cor de fundo</label>
                                            <input class="form-control ads-ad__color-picker" type="color"
                                                   value={ad.bgColor}
                                                   onChange={(e) => this.setState({
                                                       ad: {
                                                           ...ad,
                                                           bgColor: e.target.value
                                                       }
                                                   })}/>
                                        </div>
                                    </div>

                                    <div class="col-auto">
                                        <div class="form-group websites-add__form">
                                            <label>Cor do texto</label>
                                            <input id="ad-textColor" class="form-control ads-ad__color-picker"
                                                   type="color"
                                                   value={ad.textColor}
                                                   onChange={(e) => this.setState({
                                                       ad: {
                                                           ...ad,
                                                           textColor: e.target.value
                                                       }
                                                   })}/>
                                        </div>
                                    </div>

                                    <div class="col-auto">
                                        <div class="form-group websites-add__form">
                                            <label>Tamanho da fonte (px)</label>
                                            <input class="form-control ads-ad__color-picker"
                                                   type="text"
                                                   value={ad.textSize}
                                                   onChange={(e) => this.setState({
                                                       ad: {
                                                           ...ad,
                                                           textSize: e.target.value
                                                       }
                                                   })}/>
                                            <small class="form-text ad-error">
                                                {error.adTextSize}
                                            </small>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>URL alvo do Anúncio</label>
                                    <input id="ad-refUrl" class="form-control" placeholder="https://..."
                                           value={ad.refUrl}
                                           onChange={(e) => this.setState({ad: {...ad, refUrl: e.target.value}})}/>
                                    {error.adRefUrl && (
                                        <small class="form-text ad-error">
                                            {error.adRefUrl}
                                        </small>)}
                                    <small class="form-text text-muted">O usuario sera redirecionado para esse link
                                        quando o anúncio for clicado.
                                    </small>
                                </div>
                            </div>
                        )}

                        {ad.type === 'IMAGE' && (
                            <div>
                                <span class="form-text text-muted mb-3">O formato padrão da imagem é de 1.61 : 1. Por
                                    exemplo se a imagem tiver 284px de largura, a altura deve ser 176px (176 * 1.61
                                    = 284). Caso você use outro formato de imagem, ela ficará distorcida na plataforma
                                    mas correta no website que usá-la.
                                </span>

                                <div class="form-group websites-add__form">
                                    <label>URL da Imagem</label>
                                    <input id="ad-imageUrl" type="text" class="form-control"
                                           placeholder="https://..."
                                           aria-label="URL da imagem" value={ad.imageUrl}
                                           onChange={(e) => this.setState({ad: {...ad, imageUrl: e.target.value}})}/>
                                    {error.adImageUrl && (
                                        <small class="form-text ad-error">
                                            {error.adImageUrl}
                                        </small>)}
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>URL alvo do Anúncio</label>
                                    <input id="ad-refUrl" class="form-control" placeholder="https://..."
                                           value={ad.refUrl}
                                           onChange={(e) => this.setState({ad: {...ad, refUrl: e.target.value}})}/>
                                    {error.adRefUrl && (
                                        <small class="form-text ad-error">
                                            {error.adRefUrl}
                                        </small>)}
                                    <small class="form-text text-muted">O usuario sera redirecionado para esse link
                                        quando o anúncio for clicado.
                                    </small>
                                </div>
                            </div>
                        )}

                        <div class="btn dashboard-add__button mb-4"
                             onClick={this.submitAd.bind(this)}>
                            {edit_m ? 'Salvar' : 'Criar'}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export let TextAd = ({refUrl, parsedOutput, bgColor, textColor, textAlignment, textSize}) => {
    let style = `
        background-color: ${bgColor || "#f2f2f2"};
        color: ${textColor || "#000"};
        text-align: ${textAlignment};
        font-size: ${textSize}px;
    `;

    return (
        <a native href={refUrl} target="_blank" style="text-decoration: none;">
            <div class="ae-ad text" style={style}>
                {Array.isArray(parsedOutput) ? parsedOutput.map((node) => <CodeMapper {...node}/>) : ""}
            </div>
        </a>
    )
};

let CodeMapper = ({tag, content}) => (
    tag === 'b' ? (<b>{content}</b>) : tag === 'i' ? (<i>{content}</i>) : tag === 'br' ? (<br/>) : (<span>{content}</span>)
);

export let ImageAd = ({refUrl, imageUrl}) => (
    <a native href={refUrl} target="_blank">
        <div class="ae-ad">
            <img src={`${imageUrl || "https://i.imgur.com/Rf1yqaY.png"}`}/>
        </div>
    </a>
);
