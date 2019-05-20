import {Component} from "preact";
import "../../../styles/ae.css";
import {HOST} from "../../../configs";
import {AdAxiosGet, AdAxiosPost} from "../../../auth";
import {route} from "preact-router";

const DEFAULT_TEXT = "Anúncio de texto, voce pode alterar **o estilo** do texto nos __campos abaixo__.";
const DEFAULT_PARSED_OUTPUT = [{
    tag: 'span',
    content: 'Anúncio de texto, voce pode alterar '
}, {
    tag: 'b',
    content: 'o estilo '
}, {
    tag: 'span',
    content: 'do texto nos '
}, {
    tag: 'i',
    content: 'campos abaixo.'
}];

const DEFAULT_IMAGE_URL = "https://i.imgur.com/k2AxKqQ.png";

export default class CreateAdd extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            mode: 'NEW',
            ad: {
                type: 'TEXT',
                text: DEFAULT_TEXT,
                bgColor: "#f2f2f2",
                textColor: "#000",
            }
        };

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

    handleAdCheckbox(type) {
        this.setState({ad: {...this.state.ad, type: type}});
    }

    handleTextChange(e) {
        this.setState({ad: {...this.state.ad, text: e.target.value}});

        this.parseTextInput(this.state.ad.text);
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
                            <div style="display: flex;">

                                <div class="dashboard-add__blocking-container">
                                    {edit_m && ad.type !== 'TEXT' && (<div class="blocking-container"/>)}
                                    <div class="ads-ad__checkbox" onClick={this.handleAdCheckbox.bind(this, 'TEXT')}>
                                        <div class="shadow ads-ad-wrapper">
                                            <TextAd
                                                parsedOutput={ad.parsedOutput || DEFAULT_PARSED_OUTPUT}
                                                bgColor={ad.bgColor}
                                                textColor={ad.textColor}/>
                                        </div>
                                        <div
                                            class={`ads-ad__checkbox-box ${ad.type === 'TEXT' ? "active" : ""}`}/>
                                    </div>
                                </div>

                                <div class="dashboard-add__blocking-container">
                                    {edit_m && ad.type !== 'IMAGE' && (<div class="blocking-container"/>)}
                                    <div class="ads-ad__checkbox" onClick={this.handleAdCheckbox.bind(this, 'IMAGE')}>
                                        <div class="shadow ads-ad-wrapper">
                                            <ImageAd imageUrl={ad.imageUrl || DEFAULT_IMAGE_URL}/>
                                        </div>
                                        <div
                                            class={`ads-ad__checkbox-box ${ad.type === 'IMAGE' ? "active" : ""}`}/>
                                    </div>
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
                                    </div>

                                    <small class="form-text ad-error">
                                        {error.adText}
                                    </small>
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>Cor de fundo</label>
                                    <input class="form-control ads-ad__color-picker" type="color"
                                           value={ad.bgColor}
                                           onChange={(e) => this.setState({ad: {...ad, bgColor: e.target.value}})}/>
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>Cor do texto</label>
                                    <input id="ad-textColor" class="form-control ads-ad__color-picker" type="color"
                                           value={ad.textColor}
                                           onChange={(e) => this.setState({ad: {...ad, textColor: e.target.value}})}/>
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

                        <div class="btn dashboard-add__button"
                             onClick={this.submitAd.bind(this)}>
                            {edit_m ? 'Salvar' : 'Criar'}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export let TextAd = ({refUrl, parsedOutput, bgColor, textColor}) => (
    <a native href={refUrl} target="_blank" style="text-decoration: none;">
        <div class="ae-ad text"
             style={`background-color: ${bgColor || "#f2f2f2"}; color: ${textColor || "#000"};`}>
            {Array.isArray(parsedOutput) ? parsedOutput.map((node) => <CodeMapper {...node}/>) : ""}
        </div>
    </a>
);

let CodeMapper = ({tag, content}) => (
    tag === 'b' ? (<b>{content}</b>) : tag === 'i' ? (<i>{content}</i>) : (<span>{content}</span>)
);

export let ImageAd = ({refUrl, imageUrl}) => (
    <a native href={refUrl} target="_blank">
        <div class="ae-ad">
            <img src={`${imageUrl || "https://i.imgur.com/Rf1yqaY.png"}`}/>
        </div>
    </a>
);
