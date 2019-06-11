package com.harystolho.adexchange.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.payment.pagseguro.PaymentCheckoutGenerator;
import com.harystolho.adexchange.payment.pagseguro.PaymentProduct.PaymentProductType;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.JsonResponse;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class PaymentController {

	private PaymentCheckoutGenerator paymentCheckoutGenerator;

	private PaymentController(PaymentCheckoutGenerator paymentCheckoutGenerator) {
		this.paymentCheckoutGenerator = paymentCheckoutGenerator;
	}

	@PostMapping("/api/v1/payments/checkout")
	public ResponseEntity<Object> getCheckoutCode(@RequestAttribute("ae.accountId") String accountId,
			PaymentProductType product) {

		ServiceResponse<String> response = paymentCheckoutGenerator.generateCheckoutCode(product);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.of("checkoutCode", response.getReponse()).build());
		default:
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.getFullMessage());
		}
	}

}
