import {Component} from "preact";

export default class AddBalance extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <div class="websites-account-header mb-5">
                    Adicionar Saldo
                </div>

                <div class="container-fluid">
                    <div class="row">

                        {/*### 1 ###*/}
                        <div class="col-sm-12 col-md-6 col-lg-4">
                            <div class="add-balance-card shadow">
                                <div class="add-balance-card-header">
                                    <img class="add-balance-card-header-icon" src="/assets/dollar.png"/>
                                </div>
                                <div class="add-balance-card-body">
                                    <PaymentCardBody data={["23,00", "25,00"]}/>
                                </div>
                            </div>
                        </div>

                        {/*### 2 ###*/}
                        <div class="col-sm-12 col-md-6 col-lg-4">
                            <div class="add-balance-card shadow">
                                <div class="add-balance-card-header">
                                    <img class="add-balance-card-header-icon" style="left: 35%;"
                                         src="/assets/dollar.png"/>
                                    <img class="add-balance-card-header-icon" style="left: 45%;"
                                         src="/assets/dollar.png"/>
                                </div>
                                <div class="add-balance-card-body">
                                    <PaymentCardBody data={["45,00", "50,00"]}/>
                                </div>
                            </div>
                        </div>

                        {/*### 3 ###*/}
                        <div class="col-sm-12 col-md-6 col-lg-4">
                            <div class="add-balance-card shadow">
                                <div class="add-balance-card-header">
                                    <img class="add-balance-card-header-icon" style="left: 40%;"
                                         src="/assets/dollar.png"/>
                                    <img class="add-balance-card-header-icon" style="left: 30%;"
                                         src="/assets/dollar.png"/>
                                    <img class="add-balance-card-header-icon" style="left: 50%;"
                                         src="/assets/dollar.png"/>
                                </div>
                                <div class="add-balance-card-body">
                                    <PaymentCardBody data={["95,00", "100,00"]}/>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        )
    }

}

const PaymentCardBody = ({data}) => (
    <div>
        <div class="font-poppins" style="font-size: 20px;">
            <b>Saldo de R$ {data[0]}</b>
        </div>
        <div class="font-poppins mb-3 text-black-50">R$ {data[1]}</div>
        <div class="text-center">
            <a class="add-balance-card-body__buy font-raleway font-weight-bold">
                <span>Comprar</span>
            </a>
        </div>
    </div>
);