package com.harystolho.adexchange.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalRepository;
import com.harystolho.adexchange.models.Proposal;

@Service
public class ProposalRepositoryImpl implements ProposalRepository {

	private MongoOperations mongoOperations;

	public ProposalRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Proposal save(Proposal proposal) {
		return mongoOperations.save(proposal);
	}

	@Override
	public Proposal getById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));
		return mongoOperations.findOne(query, Proposal.class);
	}

	@Override
	public List<Proposal> getByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		return mongoOperations.find(query, Proposal.class);
	}

}
