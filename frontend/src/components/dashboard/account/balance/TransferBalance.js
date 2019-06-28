import {Component} from "preact";
import PaymentManager from "../../../../managers/PaymentManager";
import {SvgPagSeguro} from "../../../utils/SvgCollection";
import Account from "../Account";
import AccountManager from "../../../../managers/AccountManager";

export default class TransferBalance extends Component {
    constructor(props) {
        super(props);

        this.state = {
            error: {},
            mode: 'PAGSEGURO',
            value: '0,00',
            params: {}
        };
    }

    setTransferMode(mode) {
        this.setState({mode});
    }

    handleParamsChange(param, value) {
        this.setState({
            params: {
                ...this.state.params,
                [param]: value
            }
        })
    }

    requestMoneyWithdraw() {
        this.setState({error: {}});

        let state = this.state;

        AccountManager.requestMoneyWithdraw(state.mode, state.value, state.params).then(() => {
            this.setState({
                error: {
                    success: "Pedido de transferência feito com sucesso! Dentro de alguns dias o dinheiro será transferido para sua conta. Caso ocorra algum problema entraremos em contato com você."
                },
                value: "",
                params: {}
            });
        }).catch((err) => {
            switch (err) {
                case 'INVALID_PAYMENT_VALUE':
                    this.setState({error: {value: "Valor inválido. Deve ser uma numero maior que 0,00 e conter no maximo 2 casas decimais."}});
                    break;
                case 'INVALID_EMAIL':
                    this.setState({error: {email: "Email inválido"}});
                    break;
                case 'INVALID_CPF':
                    this.setState({error: {cpf: "CPF inválido"}});
                    break;
                case 'INVALID_BANK_CODE':
                    this.setState({error: {bankCode: "Código do banco inválido"}});
                    break;
                case 'INVALID_BANK_BRANCH':
                    this.setState({error: {accountAgency: "Agência da conta inválida"}});
                    break;
                case 'INVALID_BANK_ACCOUNT_NUMBER':
                    this.setState({error: {accountNumber: "Número da conta inválido"}});
                    break;
            }
        });
    }

    render({}, {mode, error, value, params}) {
        let transferIconClass = (type) => `balance-transfer--icon ${mode === type ? 'selected' : ''}`;

        return (
            <div class="container">
                <div class="row">
                    <div class="col websites-account-header">
                        Transferir Saldo
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col balance-transfer-msg">
                        Transferir dinheiro da plataforma para sua conta (bancária ou PagSeguro).
                    </div>
                </div>

                <div class="row no-gutters balance-transfer-icon-container shadow">

                    <div class="col-auto">
                        <div class={transferIconClass('PAGSEGURO')}
                             onClick={this.setTransferMode.bind(this, "PAGSEGURO")}>
                            <img src="/assets/balance-transfer/pagseguro.png" style={{height: 30}}/>
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class={transferIconClass('BANK')} onClick={this.setTransferMode.bind(this, "BANK")}>
                                <span style={{fontSize: 20, fontFamily: 'Quicksand'}}>
                                    Transferência Bancária
                                </span>
                        </div>
                    </div>
                </div>

                <div class="row no-gutters mt-4">
                    <div class="col-12">
                        <div class="row">
                            <div class="col-12 col-sm-auto">
                                <div class="form-group websites-add__form">
                                    <label>Valor</label>
                                    <input class="form-control" value={value}
                                           onChange={(e) => this.setState({value: e.target.value})}/>
                                    <small class="form-text text-muted ae-font-primary">
                                        O valor deve ser dividido usando uma virgula (,) em vez de um ponto (.)
                                    </small>
                                    <small class="form-text ad-error">
                                        {error.value}
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">

                    {/* PAGSEGURO START*/}
                    {mode === 'PAGSEGURO' && (
                        <div class="col-12 col-md-6">
                            <div class="form-group websites-add__form">
                                <label>Email da Conta PagSeguro</label>
                                <input class="form-control" value={params.email}
                                       onChange={(e) => this.handleParamsChange('email', e.target.value)}/>
                                <small class="form-text ad-error">
                                    {error.email}
                                </small>
                            </div>
                        </div>
                    )} {/* PAGSEGURO END*/}

                    {/* BANK START */}
                    {mode === 'BANK' && (
                        <div class="col-12">
                            <div class="row">
                                <div class="col-12 col-sm-auto">
                                    <div class="form-group websites-add__form">
                                        <label>CPF</label>
                                        <input class="form-control" value={params.cpf}
                                               onChange={(e) => this.handleParamsChange('cpf', e.target.value)}/>
                                        <small class="form-text ad-error">
                                            {error.cpf}
                                        </small>
                                    </div>
                                </div>

                                <div class="col-12 col-sm-auto">
                                    <div class="form-group websites-add__form">
                                        <label>Código do Banco</label>
                                        <input class="form-control" value={params.bankCode}
                                               onChange={(e) => this.handleParamsChange('bankCode', e.target.value)}/>
                                        <small class="form-text ad-error">
                                            {error.bankCode}
                                        </small>
                                    </div>
                                </div>

                                <div class="col-12 col-sm-auto">
                                    <div class="form-group websites-add__form">
                                        <label>Agência da Conta</label>
                                        <input class="form-control" value={params.accountAgency}
                                               onChange={(e) => this.handleParamsChange('accountAgency', e.target.value)}/>
                                        <small class="form-text ad-error">
                                            {error.accountAgency}
                                        </small>
                                    </div>
                                </div>

                                <div class="col-12 col-sm-auto">
                                    <div class="form-group websites-add__form">
                                        <label>Número da Conta</label>
                                        <input class="form-control" value={params.accountNumber}
                                               onChange={(e) => this.handleParamsChange('accountNumber', e.target.value)}/>
                                        <small class="form-text ad-error">
                                            {error.accountNumber}
                                        </small>
                                    </div>
                                </div>
                            </div>

                        </div>
                    )} {/* BANK END */}
                </div>

                {error.success && (
                    <div class="row mt-4">
                        <div class="col">
                            <div class="shadow-sm dashboard-success-container dashboard-container-wrapper">
                                <dl>
                                    <dt>Sucesso</dt>
                                    <dd>
                                        {error.success}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                )}

                <div class="row mt-4">
                    <div class="col-auto">
                        <div class="balance-transfer-btn dashboard-website__rounded-button mb-3"
                             onClick={() => this.requestMoneyWithdraw()}>
                            Fazer Transferência
                        </div>
                    </div>
                </div>

            </div>
        )
    }
}