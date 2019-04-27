package com.harystolho.adexchange.dao.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalsHolderRepository;
import com.harystolho.adexchange.models.ProposalsHolder;

@Service
public class ProposalsHolderRepositoryImpl implements ProposalsHolderRepository {

	private MongoOperations mongoOperations;

	public ProposalsHolderRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public ProposalsHolder save(ProposalsHolder proposalsHolder) {
		return mongoOperations.save(proposalsHolder);
	}

	@Override
	public ProposalsHolder getByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		return mongoOperations.findOne(query, ProposalsHolder.class);
	}

}
