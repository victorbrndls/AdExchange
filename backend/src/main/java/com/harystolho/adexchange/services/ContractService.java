package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.repositories.contract.ContractRepository;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.models.Proposal;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	public ContractService(ContractRepository contractRepository) {
		this.contractRepository = contractRepository;
	}

	private Contract createContract(String creatorId, String acceptorId, String websiteId, String adId,
			PaymentMethod paymentMethod, String paymentValue, int duration) {

		Contract contract = new Contract();
		contract.setCreatorId(creatorId);
		contract.setAcceptorId(acceptorId);
		contract.setWebsiteId(websiteId);
		contract.setAdId(adId);
		contract.setPaymentMethod(paymentMethod);
		contract.setPaymentValue(paymentValue);
		contract.setExpiration(LocalDateTime.now().plusDays(duration));

		contractRepository.save(contract);

		return contract;
	}

	public void createContractFromProposal(Proposal prop, String creatorId, String acceptorId) {
		createContract(creatorId, acceptorId, prop.getWebsiteId(), prop.getAdId(), prop.getPaymentMethod(),
				prop.getPaymentValue(), prop.getDuration());
	}

	public ServiceResponse<Contract> getContractById(String accountId, String id) {
		Contract contract = contractRepository.getById(id);

		return ServiceResponse.ok(contract);
	}

	public ServiceResponse<List<Contract>> getContractsByAccountId(String accountId) {
		List<Contract> contracts = contractRepository.getByAccountId(accountId);

		return ServiceResponse.ok(contracts);
	}

}
