package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.contract.ContractRepository;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	private UserDataService userDataService;

	@Autowired
	public ContractService(ContractRepository contractRepository, UserDataService userDataService) {
		this.contractRepository = contractRepository;
		this.userDataService = userDataService;
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

		userDataService.addContract(creatorId, contract.getId());
		userDataService.addContract(acceptorId, contract.getId());

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
		List<String> contractsId = userDataService.getContractsByAccountId(accountId);

		List<Contract> contracts = new ArrayList<>();

		contractsId.forEach((c) -> {
			contracts.add(getContractById(accountId, c).getReponse());
		});

		return ServiceResponse.ok(contracts);
	}

}
