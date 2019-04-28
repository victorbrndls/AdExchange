package com.harystolho.adexchange.repositories.contract;

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

}
