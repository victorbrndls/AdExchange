package com.harystolho.adexchange.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.contract.ContractRepository;

@Service
public class ContractService {

	private ContractRepository contractRepository;

	private AdService adService;
	private WebsiteService websiteService;

	@Autowired
	public ContractService(ContractRepository contractRepository, AdService adService, WebsiteService websiteService) {
		this.contractRepository = contractRepository;
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

		return contract;
	}

	public void createContractFromProposal(Proposal prop, String creatorId, String acceptorId) {
		createContract(creatorId, acceptorId, prop.getWebsiteId(), prop.getAdId(), prop.getPaymentMethod(),
				prop.getPaymentValue(), prop.getDuration());
	}

	public ServiceResponse<Contract> getContractById(String accountId, String id) {
		Contract contract = contractRepository.getById(id);

		if (!contract.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		return ServiceResponse.ok(contract);
	}

	public ServiceResponse<List<Contract>> getContractsByAccountId(String accountId, String embed) {
		List<Contract> contracts = contractRepository.getByAccountId(accountId);

		if (embed.contains("website"))
			contracts.forEach((contract) -> {
				contract.setWebsite(websiteService.getWebsiteById(contract.getWebsiteId()).getReponse());
			});

		return ServiceResponse.ok(contracts);
	}

	/**
	 * 
	 * @param accountId
	 * @param ids
	 * @param embed
	 * @return a list of {@link Contract contracts} and the embeded fields
	 */
	public ServiceResponse<List<Contract>> getContractsById(String accountId, String ids) {
		List<Contract> contracts = contractRepository.getManyById(StringUtils.commaDelimitedListToSet(ids));

		return ServiceResponse.ok(
				contracts.stream().filter(contract -> contract.isAuthorized(accountId)).collect(Collectors.toList()));
	}

	/**
	 * @param accountId
	 * @return The contracts that haven't expired and were accepted by the user for
	 *         his/her websites
	 */
	public ServiceResponse<List<Contract>> getContractsForUserWebisites(String accountId) {
		List<Contract> contracts = contractRepository.getByAcceptorId(accountId);

		return ServiceResponse.ok(contracts.stream().filter(c -> !c.hasExpired()).collect(Collectors.toList()));
	}

	public ServiceResponse<Contract> updateContract(String accountId, String id, String name) {
		Contract contract = contractRepository.getById(id);

		if (contract == null)
			return ServiceResponse.fail("INVALID_CONTRACT_ID");

		if (!contract.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		if (StringUtils.hasText(name)) {
			if (contract.getCreatorId().equals(accountId)) {
				contract.setCreatorContractName(name);
			} else if (contract.getAcceptorId().equals(accountId)) {
				contract.setAcceptorContractName(name);
			}
		}

		contractRepository.save(contract);

		return ServiceResponse.ok(contract);
	}
}
