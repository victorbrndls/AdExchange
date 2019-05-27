package com.harystolho.adexchange.services.payment;

import java.util.Collection;

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
 * Checks wheater a payment to the website owner should be made
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

	/**
	 * 
	 * @param contractId
	 * @param allowedMethods only issue payment if the
	 *                       {@link Contract#getPaymentMethod()} is one of the
	 *                       {allowedMethods}
	 */
	public void issueContractPayment(String contractId, Collection<PaymentMethod> allowedMethods) {
		ServiceResponse<Contract> response = contractService.getContractById(AEUtils.ADMIN_ACCESS_ID, contractId);

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("Error while issuing payment. The contract id is not valid. contractId: [%s]", contractId);
			return;
		}

		issueContractPayment(response.getReponse(), allowedMethods);
	}

	private void issueContractPayment(Contract contract, Collection<PaymentMethod> allowedMethods) {
		if (!verifyPaymentMethod(contract.getPaymentMethod(), allowedMethods))
			return;

		String payerId = contract.getCreatorId();
		String recieverId = contract.getAcceptorId();
		String value = contract.convertPaymentValueToDotNotation();

		accountService.transferBalance(payerId, recieverId, value);
	}

	private boolean verifyPaymentMethod(PaymentMethod paymentMethod, Collection<PaymentMethod> allowedMethods) {
		return allowedMethods.contains(paymentMethod);
	}

}
