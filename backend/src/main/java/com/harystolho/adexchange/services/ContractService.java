package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.contract.ContractRepository;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	private AdService adService;
	private WebsiteService websiteService;
	private UserDataService userDataService;

	@Autowired
	public ContractService(ContractRepository contractRepository, UserDataService userDataService, AdService adService,
			WebsiteService websiteService) {
		this.contractRepository = contractRepository;
		this.userDataService = userDataService;
		this.adService = adService;
		this.websiteService = websiteService;
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

		return ServiceResponse.ok(contractRepository.getManyById(contractsId));
	}

	/**
	 * 
	 * @param accountId
	 * @param ids
	 * @param embed
	 * @return a list of {@link Contract contracts} and the embeded fields
	 */
	public ServiceResponse<List<ObjectNode>> getContractsById(String accountId, String ids, String embed) {
		List<Contract> contracts = contractRepository.getManyById(Arrays.asList(ids.split(",")));

		ObjectMapper mapper = new ObjectMapper();

		List<ObjectNode> belongsToUser = contracts.stream().filter((contract) -> {
			return contract.getAcceptorId().equals(accountId) || contract.getCreatorId().equals(accountId);
		}).map((c) -> {
			ObjectNode node = (ObjectNode) mapper.valueToTree(c);

			// The mapper maps the LocalDateTime object to json and that is not needed,
			// replace that with the normal value
			node.put("expiration", c.getExpiration().toString());

			if (embed.contains("website")) {
				node.remove("websiteId");
				node.set("website", mapper.valueToTree(websiteService.getWebsiteById(c.getWebsiteId()).getReponse()));
			}

			if (embed.contains("ad")) {
				node.remove("adId");
				node.set("ad", mapper.valueToTree(adService.getAdById(c.getAdId()).getReponse()));
			}

			return node;
		}).collect(Collectors.toList());

		return ServiceResponse.ok(belongsToUser);
	}

	public ServiceResponse<List<Contract>> getContractsForUserWebisites(String accountId) {
		List<Contract> contracts = contractRepository.getByAcceptorId(accountId);
		return ServiceResponse.ok(contracts);
	}

}
