package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.contract.ContractRepository;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	private AdService adService;
	private UserDataService userDataService;

	@Autowired
	public ContractService(ContractRepository contractRepository, UserDataService userDataService,
			AdService adService) {
		this.contractRepository = contractRepository;
		this.userDataService = userDataService;
		this.adService = adService;
	}

	private Contract createContract(String creatorId, String acceptorId, String websiteId, String adId,
			PaymentMethod paymentMethod, String paymentValue, int duration) {

		Contract contract = new Contract();
		contract.setCreatorId(creatorId);
		contract.setAcceptorId(acceptorId);
		contract.setWebsiteId(websiteId);
		contract.setPaymentMethod(paymentMethod);
		contract.setPaymentValue(paymentValue);
		contract.setExpiration(LocalDateTime.now().plusDays(duration));

		// Duplicate the Ad to make sure it doesn't change
		String finalAd = adService.duplicateAd(adId);
		contract.setAdId(finalAd);

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

	public ServiceResponse<List<Contract>> getContractsById(String accountId, String ids) {
		List<Contract> contracts = contractRepository.getManyById(Arrays.asList(ids.split(",")));

		return ServiceResponse.ok(contracts.stream().filter((contract) -> {
			return contract.getAcceptorId().equals(accountId) || contract.getCreatorId().equals(accountId);
		}).collect(Collectors.toList()));
	}

}
