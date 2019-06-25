import {Link} from "preact-router";

const Header = ({view = 'ADVERTISER'}) => {
    return (
        <div class="header shadow-sm">
            <div class="container justify-content-around justify-content-md-end">
                <div class="ae-logo">
                    <a href="/">
                        <img src="/assets/logo.png" class="ae-logo--img"/>
                    </a>
                </div>
                <div class="ae-menu">
                    <div class="ae-navbar--page">
                        {view === 'WEBSITE' ? (
                            <Link href="/">Para Anunciantes</Link>
                        ) : (
                            <Link href="/website">Para Donos de Website</Link>
                        )}
                    </div>
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
    )
};

export default Header;