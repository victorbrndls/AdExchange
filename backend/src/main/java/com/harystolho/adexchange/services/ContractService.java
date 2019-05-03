package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	public ServiceResponse<List<Contract>> getContractsById(String accountId, String ids, String embed) {
		List<Contract> contracts = contractRepository.getManyById(Arrays.asList(ids.split(",")));

		return ServiceResponse.ok(contracts.stream().filter(contract -> contract.isAuthorized(accountId)).map((c) -> {
			if (embed.contains("website")) {
				c.setWebsite(websiteService.getWebsiteById(c.getWebsiteId()).getReponse());
			}

			if (embed.contains("ad")) {
				c.setAd(adService.getAdById(c.getAdId()).getReponse());
			}

			return c;
		}).collect(Collectors.toList()));
	}

	public ServiceResponse<List<ObjectNode>> getContractsForUserWebisites(String accountId, String embed) {
		List<Contract> contracts = contractRepository.getByAcceptorId(accountId);

		ObjectMapper mapper = new ObjectMapper();

		return ServiceResponse.ok(contracts.stream().map((c) -> {
			ObjectNode node = (ObjectNode) mapper.valueToTree(c);

			if (embed.contains("website")) {
				node.remove("website");
				node.set("website", mapper.valueToTree(websiteService.getWebsiteById(c.getWebsiteId()).getReponse()));
			}

			return node;
		}).collect(Collectors.toList()));
	}

	public ServiceResponse<Contract> updateContract(String accountId, String id, String name) {
		Contract contract = contractRepository.getById(id);

		if (contract == null)
			return ServiceResponse.fail("INVALID_CONTRACT_ID");

		if (!contract.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		if (StringUtils.hasText(name)) {
			if (contract.getCreatorId().equals(accountId)) {
				contract.setCreatorContractName(sanitizeContractName(name));
			} else if (contract.getAcceptorId().equals(accountId)) {
				contract.setAcceptorContractName(sanitizeContractName(name));
			}
		}

		contractRepository.save(contract);

		return ServiceResponse.ok(contract);
	}

	private String sanitizeContractName(String name) {
		return name;
	}
}
