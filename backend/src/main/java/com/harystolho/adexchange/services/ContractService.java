package com.harystolho.adexchange.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.repositories.contract.ContractRepository;
import com.harystolho.adexchange.models.Proposal;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	public ContractService(ContractRepository contractRepository) {
		this.contractRepository = contractRepository;
	}

	private Contract createContract(String websiteId, String adId, PaymentMethod paymentMethod, String paymentValue,
			int duration) {

		Contract contract = new Contract();
		contract.setWebsiteId(websiteId);
		contract.setAdId(adId);
		contract.setPaymentMethod(paymentMethod);
		contract.setPaymentValue(paymentValue);
		contract.setExpiration(LocalDateTime.now().plusDays(duration));

		contractRepository.save(contract);

		return contract;
	}

	public void createContractFromProposal(Proposal prop) {
		createContract(prop.getWebsiteId(), prop.getAdId(), prop.getPaymentMethod(), prop.getPaymentValue(),
				prop.getDuration());
	}

}
