package com.harystolho.adexchange.repositories.contract;

import java.util.Collection;
import java.util.List;

import com.harystolho.adexchange.models.Contract;

public interface ContractRepository {

	void save(Contract contract);

	Contract getById(String id);

	List<Contract> getManyById(Collection<String> ids);

	/**
	 * @param accountId
	 * @return the contracts or an empty list
	 */
	List<Contract> getByAccountId(String accountId);

	List<Contract> getByAcceptorId(String accountId);

	void remove(String id);

}
