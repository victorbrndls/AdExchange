package com.harystolho.adserver.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.harystolho.adserver.data.AdModelData;
import com.harystolho.adserver.data.AdModelDataCache;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.AccountService;

/**
 * Removes Spots from {@link AdModelService} if the cached Spot is not suitable
 * for distribution
 * 
 * @author Harystolho
 *
 */
@Service
public class SpotUpdater {

	private AdModelDataCache adModelCache;
	private AccountService accountService;
	private AdModelService adModelService;

	public SpotUpdater(AdModelDataCache dataCacheContainer, AccountService accountService,
			AdModelService adModelService) {
		this.adModelCache = dataCacheContainer;
		this.accountService = accountService;
		this.adModelService = adModelService;
	}

	/**
	 * This method is called when the account balance changes and for that reason
	 * the user may not have enough balance to pay for more ads. This method updates
	 * all the spots that are bound to contracts owned by the {accountId} and
	 * removes the ones that the user can't pay for
	 * 
	 * @param accountId
	 */
	public void updateSpotsAdvertisedByUser(String accountId) {
		AdModelData dc = adModelCache.get(accountId);

		if (dc == null)
			return; // There are no ads being displayed that are payed by this user

		Set<Contract> contractsToBeRemoved = new HashSet<>();
		Set<Spot> spotsToBeRemoved = new HashSet<>();

		// Contracts that the user doesn't have balance to pay
		for (Contract c : dc.getContracts()) {
			if (!accountService.hasAccountBalance(accountId, c.convertPaymentValueToDotNotation())) {
				contractsToBeRemoved.add(c);
			}
		}

		// Spots that are bound to contracts that the user doesn't have balance to pay
		for (Spot s : dc.getSpots()) {
			for (Contract c : contractsToBeRemoved) {
				if (c.getId().equals(s.getContractId())) {
					spotsToBeRemoved.add(s);
					continue;
				}
			}
		}

		dc.removeContracts(contractsToBeRemoved);
		dc.removeSpots(spotsToBeRemoved);

		if (dc.isEmpty())
			adModelCache.remove(accountId);

		spotsToBeRemoved.forEach(s -> adModelService.updateSpot(s));
	}

}
