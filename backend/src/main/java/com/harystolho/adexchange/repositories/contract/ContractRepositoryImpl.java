package com.harystolho.adexchange.repositories.contract;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;

@Service
public class ContractRepositoryImpl implements ContractRepository {

	private MongoOperations mongoOperations;

	public ContractRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void save(Contract contract) {
		mongoOperations.save(contract);
	}

	@Override
	public Contract getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contract> getByAccountId(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
