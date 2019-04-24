import {Component} from "preact";
import "../../../styles/ae.css";

export default class CreateAdd extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
            adType: "TEXT"
        };
    }

    handleAdCheckbox(type) {
        this.setState({adType: type});
    }

    updateAdBgColor(e) {
        document.getElementsByClassName('ae-ad text')[0].style.backgroundColor = e.target.value;
    }

    render({}, {error}) {
        return (
            <div>
                <div class="websites-add__container">
                    <div style="font-family: Raleway; font-size: 30px;">
                        Adicionar Anúncio
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
                            <input id="ad-name" class="form-control" maxLength="60"/>
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

                        <div>
                            {this.state.adType === 'TEXT' && (
                                <div>
                                    <div class="form-group websites-add__form">
                                        <label>Texto</label>
                                        <input id="ad-text" class="form-control"/>
                                    </div>

                                    <div class="form-group websites-add__form">
                                        <label>Cor de fundo</label>
                                        <input id="ad-bgColor" class="form-control" type="color"
                                               style="width: 55px;height: 35px;" value="#FFD700"
                                               onChange={this.updateAdBgColor.bind(this)}/>
                                    </div>
                                </div>
                            )}

                            {this.state.adType === 'IMAGE' && (
                                <div class="form-group websites-add__form">
                                    <label>URL da Imagem</label>
                                    <div class="input-group mb-3">
                                        <input type="text" class="form-control" placeholder="https://..."
                                               aria-label="URL da imagem"/>
                                        <div class="input-group-append">
                                            <button class="btn btn-outline-secondary" type="button" id="ad-loadImage">
                                                Carregar Imagem
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>


                        <div class="btn"
                             style="background-color: #156dc9; color: white; font-size: 17px; font-weight: bold;">
                            Adicionar
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