import {Link} from "preact-router";

const Footer = () => (
    <div class="ae-home-footer shadow-lg">
        <div class="container ae-home py-3">
            <div class="row">
                <div class="col-xs-12 col-md-auto ae-logo ae-footer-logo">
                    <img src="/assets/logo.png" class="ae-logo--img"/>
                </div>

                <div class="col-sm-12 col-md ae-footer">
                    <div class="row align-items-center h-100 ae-footer-info">
                        <a native class="col-auto" href="mailto:tidderjail2@gmail.com">Contato</a>
                        <Link class="col-auto">Política de Privacidade</Link>
                    </div>
                </div>

                <div class="col-sm-12 col-md align-self-center ae-footer-copyright">
                                    <span class="text-black-50">
                                        © 2019 Adnamic. Todos os direitos reservados
                                    </span>
                </div>
            </div>
        </div>
    </div>
);

export default Footer;