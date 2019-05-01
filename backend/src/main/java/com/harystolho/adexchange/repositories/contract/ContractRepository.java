package com.harystolho.adexchange.repositories.contract;

import java.util.List;

import com.harystolho.adexchange.models.Contract;

public interface ContractRepository {

	void save(Contract contract);

	Contract getById(String id);

	List<Contract> getManyById(List<String> ids);

	/**
	 * @param accountId
	 * @return the contracts or an empty list
	 */
	List<Contract> getByAccountId(String accountId);

	List<Contract> getByAcceptorId(String accountId);

}
