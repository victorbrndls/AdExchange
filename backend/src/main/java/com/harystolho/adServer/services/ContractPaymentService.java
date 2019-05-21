package com.harystolho.adServer.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract.PaymentMethod;

/**
 * Transfers money from the contract owner to the website owner
 * 
 * @author Harystolho
 *
 */
@Service
public class ContractPaymentService {

	/**
	 * Verifies if a payment to the website owner should be done. For the payment to
	 * occur the user can't have seen the ad before if the contract payment method
	 * is {@link PaymentMethod#PAY_PER_VIEW} or clicked the ad before if the
	 * contract payment method is {@link PaymentMethod#PAY_PER_CLICK}
	 * 
	 * @param contractId
	 */
	public void issueContractPayment(String contractId) {
		System.out.println("paying: " + contractId);
	}

}
