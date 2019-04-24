import {Component} from "preact";
import "../../../styles/ae.css";

export default class CreateAdd extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            adType: "TEXT"
        };

        this.fields = {
            adName: () => document.getElementById("ad-name"),
            adText: () => document.getElementById('ad-text'),
            adBgColor: () => document.getElementById('ad-bgColor'),
            adTextColor: () => document.getElementById('ad-textColor'),
            adRefUrl: () => document.getElementById('ad-refUrl'),
            adImageUrl: () => document.getElementById('ad-imageUrl')
        };

        this.ad = () => document.getElementsByClassName('ae-ad text')[0];
    }

    handleAdCheckbox(type) {
        this.setState({adType: type});
    }

    updateAdBgColor(e) {
        this.ad().style.backgroundColor = e.target.value;
    }

    updateAdTextColor(e) {
        this.ad().style.color = e.target.value;
    }

    handleSubmit() {
        switch (this.state.adType) {
            case 'TEXT':
                this.submitTextAd();
                return;
            case 'IMAGE':
                this.submitImageAd();
                return;
            default:
                return;
        }
    }

    submitTextAd() {
        if (!this.verifyTextAdFields())
            return;

        
    }

    verifyTextAdFields() {
        this.setState({error: {}});

        if (!this.verifyAdName())
            return false;

        if (this.fields.adText().value.trim().length < 5) {
            this.setState({error: {...this.state.error, adText: "O texto deve conter pelo menos 5 caracteres"}});
            return false;
        }

        if (!this.verifyRefUrl())
            return false;

        return true;
    }

    submitImageAd() {
        if (!this.verifyImageAdFields())
            return;
    }

    verifyImageAdFields() {
        this.setState({error: {}});

        if (!this.verifyAdName())
            return false;

        if (this.fields.adImageUrl().value.match(/(https:\/\/)|(http:\/\/)/g) === null) {
            this.setState({error: {...this.state.error, adImage: "O URL da imagem nao e' valido"}});
            return false;
        }

        if (!this.verifyRefUrl())
            return false;

        return true;
    }

    verifyAdName() {
        if (this.fields.adName().value.trim().length < 5) {
            this.setState({
                error: {
                    ...this.state.error,
                    adName: "O nome do anuncio deve conter pelo menos 5 caracteres"
                }
            });
            return false;
        }

        return true;
    }

    verifyRefUrl() {
        if (this.fields.adRefUrl().value.match(/(https:\/\/)|(http:\/\/)/g) === null) {
            this.setState({error: {...this.state.error, adRefUrl: "URL invalido"}});
            return false;
        }

        return true;
    }

    render({}, state) {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Criar Anúncio
                    </div>

                    <div style="margin-top: 5px;">
                        <div class="form-group websites-add__form">
                            <label>Nome</label>
                            <input id="ad-name" class="form-control" maxLength="60"/>
                            {state.error.adName && (
                                <small class="form-text ad-error">
                                    {state.error.adName}
                                </small>)}
                        </div>

                        <div class="form-group websites-add__form">
                            <label>Modelo do Anúncio</label>
                            <div style="display: flex;">
                                <div class="ads-ad__checkbox" onClick={this.handleAdCheckbox.bind(this, 'TEXT')}>
                                    <div class="shadow ads-ad-wrapper">
                                        <TextAd/>
                                    </div>
                                    <div
                                        class={`ads-ad__checkbox-box ${this.state.adType === 'TEXT' ? "active" : ""}`}/>
                                </div>

                                <div class="ads-ad__checkbox" onClick={this.handleAdCheckbox.bind(this, 'IMAGE')}>
                                    <div class="shadow ads-ad-wrapper">
                                        <ImageAd/>
                                    </div>
                                    <div
                                        class={`ads-ad__checkbox-box ${this.state.adType === 'IMAGE' ? "active" : ""}`}/>
                                </div>
                            </div>
                        </div>

                        {this.state.adType === 'TEXT' && (
                            <div>
                                <div class="form-group websites-add__form">
                                    <label>Texto</label>
                                    <input id="ad-text" class="form-control"/>
                                    {state.error.adText && (
                                        <small class="form-text ad-error">
                                            {state.error.adText}
                                        </small>)}
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>Cor de fundo</label>
                                    <input id="ad-bgColor" class="form-control ads-ad__color-picker" type="color"
                                           value="#FFD700"
                                           onChange={this.updateAdBgColor.bind(this)}/>
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>Cor do texto</label>
                                    <input id="ad-textColor" class="form-control ads-ad__color-picker" type="color"
                                           value="#000"
                                           onChange={this.updateAdTextColor.bind(this)}/>
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>URL alvo do Anúncio</label>
                                    <input id="ad-refUrl" class="form-control" placeholder="https://..."/>
                                    {state.error.adRefUrl && (
                                        <small class="form-text ad-error">
                                            {state.error.adRefUrl}
                                        </small>)}
                                    <small class="form-text text-muted">O usuario sera redirecionado para esse link
                                        quando o anuncio for clicado.
                                    </small>
                                </div>
                            </div>
                        )}

                        {this.state.adType === 'IMAGE' && (
                            <div>
                                <div class="form-group websites-add__form">
                                    <label>URL da Imagem</label>
                                    <div class="input-group mb-3">
                                        <input id="ad-imageUrl" type="text" class="form-control"
                                               placeholder="https://..."
                                               aria-label="URL da imagem"/>
                                        <div class="input-group-append">
                                            <button class="btn btn-outline-secondary" type="button"
                                                    id="ad-loadImage">
                                                Carregar Imagem
                                            </button>
                                        </div>
                                    </div>
                                    {state.error.adImage && (
                                        <small class="form-text ad-error">
                                            {state.error.adImage}
                                        </small>)}
                                </div>

                                <div class="form-group websites-add__form">
                                    <label>URL alvo do Anúncio</label>
                                    <input id="ad-refUrl" class="form-control" placeholder="https://..."/>
                                    {state.error.adRefUrl && (
                                        <small class="form-text ad-error">
                                            {state.error.adRefUrl}
                                        </small>)}
                                    <small class="form-text text-muted">O usuario sera redirecionado para esse link
                                        quando o anuncio for clicado.
                                    </small>
                                </div>
                            </div>
                        )}

                        <div class="btn"
                             style="background-color: #156dc9; color: white; font-size: 17px; font-weight: bold;"
                             onClick={this.handleSubmit.bind(this)}>
                            Criar
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

let TextAd = ({}) => (
    <div class="ae-ad text">
        Utilize texto para fazer o seu anuncio. Quando o texto for clicado, uma nova jenela abrira no navegador
    </div>
);

let ImageAd = ({}) => (
    <div class="ae-ad">
        <img src="https://intravert.co/static/images/img_7.jpg"/>
    </div>
);