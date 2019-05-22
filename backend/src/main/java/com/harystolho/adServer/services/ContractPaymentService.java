package com.harystolho.adServer.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.ContractService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;

/**
 * Transfers money from the contract owner to the website owner
 * 
 * @author Harystolho
 *
 */
@Service
public class ContractPaymentService {

	private ContractService contractService;
	private AccountService accountService;
	private Logger logger;

	public ContractPaymentService(ContractService contractService, AccountService accountService, Logger logger) {
		this.contractService = contractService;
		this.accountService = accountService;
		this.logger = logger;
	}

	public void issueContractPayment(String contractId) {
		ServiceResponse<Contract> response = contractService.getContractById(AEUtils.ADMIN_ACESS_ID, contractId);

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("Error while issuing payment. The contract id is not valid. contractId: [%s]", contractId);
			return;
		}

		issueContractPayment(response.getReponse());
	}

	private void issueContractPayment(Contract contract) {
		if (!verifyPaymentMethod(contract.getPaymentMethod()))
			return;

		String payerId = contract.getCreatorId();
		String recieverId = contract.getAcceptorId();
		String value = contract.getPaymentValue();

		accountService.transferBalance(payerId, recieverId, value);
	}

	/**
	 * The payment should only if the the payment method is one of the below
	 * 
	 * @param paymentMethod
	 * @return
	 */
	private boolean verifyPaymentMethod(PaymentMethod paymentMethod) {
		return paymentMethod == PaymentMethod.PAY_PER_CLICK || paymentMethod == PaymentMethod.PAY_PER_VIEW;
	}

}
