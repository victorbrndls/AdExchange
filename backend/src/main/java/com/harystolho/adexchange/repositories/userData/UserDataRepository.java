package com.harystolho.adexchange.repositories.userData;

import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.models.UserData;

public interface UserDataRepository {

	UserData save(UserData userData);

	ProposalsHolder saveProposalsHolder(ProposalsHolder ph);

	ProposalsHolder getProposalsHolderByAccountId(String accountId);

	/**
	 * @param accountId
	 * @return TRUE if a {@link UserData} exists for the given accountId
	 */
	boolean exists(String accountId);

}
