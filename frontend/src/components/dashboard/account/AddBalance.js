import {Component} from "preact";
import PaymentManager from "../../../managers/PaymentManager";

export default class AddBalance extends Component {
    constructor(props) {
        super(props);
    }

    displayCheckout(balanceProduct) {
        PaymentManager.getCheckoutCode(balanceProduct).then((data) => {
            let code = data.checkoutCode;

            PagSeguroLightbox(code);

            console.log(data);
        }).catch((error) => {
            switch (error){
                case 'FAIL/':
                    //TODO show error message
                    break;
            }
        });
    }

    render() {
        return (
            <div class="container">
                <div class="row mb-5">
                    <div class="col websites-account-header">
                        Adicionar Saldo
                    </div>
                </div>

                <div class="row justify-content-center">
                    <PaymentCard leftOffset={[40]}
                                 data={["23,00", "25,00", this.displayCheckout.bind(this, PaymentManager.BALANCE_PRODUCT.BALANCE_25)]}/>
                    <PaymentCard leftOffset={[35, 45]}
                                 data={["45,00", "50,00", this.displayCheckout.bind(this, PaymentManager.BALANCE_PRODUCT.BALANCE_50)]}/>
                    <PaymentCard leftOffset={[30, 50, 40]}
                                 data={["95,00", "100,00", this.displayCheckout.bind(this, PaymentManager.BALANCE_PRODUCT.BALANCE_100)]}/>
                </div>

                <script type="text/javascript"
                        src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.lightbox.js">
                </script>
            </div>
        )
    }
}

const PaymentCard = ({leftOffset, data}) => (
    <div class="col-sm-12 col-md-6 col-xl-4">
        <div class="add-balance-card shadow">
            <div class="add-balance-card-header">
                <PaymentCardIcon leftOffset={leftOffset}/>
            </div>
            <div class="add-balance-card-body">
                <PaymentCardBody data={data}/>
            </div>
        </div>
    </div>)
;

const PaymentCardBody = ({data}) => (
    <div>
        <div class="font-poppins" style="font-size: 20px;">
            <b>Saldo de R$ {data[0]}</b>
        </div>
        <div class="font-poppins mb-3 text-black-50">R$ {data[1]}</div>
        <div class="text-center">
            <span class="add-balance-card-body__buy font-raleway font-weight-bold" onClick={() => data[2]()}>
                <span>Comprar</span>
            </span>
        </div>
    </div>
);

const PaymentCardIcon = ({leftOffset = []}) => (
    <div>
        {leftOffset.map((offset) => (
            <img class="add-balance-card-header-icon" style={`left: ${offset}%;`}
                 src="/assets/dollar.png"/>
        ))}
    </div>
);