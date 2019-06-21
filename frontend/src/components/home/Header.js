import {Link} from "preact-router";

const Header = () => (
    <div class="header shadow-sm">
        <div class="container">
            <div class="ae-logo">
                <img src="/assets/logo.png"/>
            </div>
            <div class="ae-navbar">
                <div class="ae-navbar--link">
                    <Link href="/auth?login">Entrar</Link>
                </div>
                <div class="ae-navbar--link ae-navbar-account__new">
                    <Link href="/auth?register">Criar Conta</Link>
                </div>
            </div>
        </div>
    </div>
);

export default Header;