package com.harystolho.adexchange.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.admin.AdminRepository;
import com.harystolho.adexchange.controllers.models.BalanceWithdrawModel;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;

@Service
public class BalanceService {

	private final AdminRepository adminRepository;

	public enum WithdrawModes {
		PAGSEGURO, BANK
	}

	private BalanceService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	public ServiceResponseType requestBalanceWithdraw(BalanceWithdrawModel model) {
		ServiceResponseType res = verifyBalanceWithdrawRequestFields(model);
		if (res != ServiceResponseType.OK)
			return res;

		adminRepository.saveBalanceWithdrawRequest(model);

		return ServiceResponseType.OK;
	}

	private ServiceResponseType verifyBalanceWithdrawRequestFields(BalanceWithdrawModel model) {
		WithdrawModes mode = null;

		try {
			mode = WithdrawModes.valueOf(model.getMode());
		} catch (Exception e) {
			return ServiceResponseType.INVALID_BALANCE_TRANSFER_MODE;
		}

		if (!AEUtils.validateMonetaryValue(model.getValue()))
			return ServiceResponseType.INVALID_PAYMENT_VALUE;

		if (mode == WithdrawModes.PAGSEGURO) {
			if (!AEUtils.verifyEmail(model.getEmail()))
				return ServiceResponseType.INVALID_EMAIL;

		} else if (mode == WithdrawModes.BANK) {
			if (!StringUtils.hasText(model.getCpf()))
				return ServiceResponseType.INVALID_CPF;

			if (!StringUtils.hasText(model.getBankCode()))
				return ServiceResponseType.INVALID_BANK_CODE;

			if (!StringUtils.hasText(model.getAccountAgency()))
				return ServiceResponseType.INVALID_BANK_BRANCH;

			if (!StringUtils.hasText(model.getAccountNumber()))
				return ServiceResponseType.INVALID_BANK_ACCOUNT_NUMBER;

		}

		return ServiceResponseType.OK;
	}

}
