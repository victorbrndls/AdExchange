package com.harystolho.adexchange.payment.pagseguro;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.payment.pagseguro.PaymentProduct.PaymentProductType;

/**
 * Stores all the products that can be bought
 * 
 * @author Harystolho
 *
 */
@Service
public class ProductRegistry {

	private final Map<PaymentProductType, PaymentProduct> typeToProduct;

	private ProductRegistry() {
		this.typeToProduct = new HashMap<>();

		this.typeToProduct.put(PaymentProductType.BALANCE_25, new PaymentProduct.PaymentProductBuilder()
				.id("BALANCE_25").description("Saldo de RS 23.00").amount("25.00").quantity("1").weight("1").build());

		this.typeToProduct.put(PaymentProductType.BALANCE_50, new PaymentProduct.PaymentProductBuilder()
				.id("BALANCE_50").description("Saldo de RS 45.00").amount("50.00").quantity("1").weight("1").build());

		this.typeToProduct.put(PaymentProductType.BALANCE_100, new PaymentProduct.PaymentProductBuilder()
				.id("BALANCE_100").description("Saldo de RS 95.00").amount("100.00").quantity("1").weight("1").build());
	}

	/**
	 * @param type
	 * @return the product or <code>null</code>
	 */
	public PaymentProduct getProduct(PaymentProductType type) {
		return typeToProduct.get(type);
	}

}
