package com.harystolho.adexchange.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalsHolderRepository;
import com.harystolho.adexchange.models.Proposal;
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

	@Override
	public void addProposalToSent(String accountId, String proposalId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		Update update = new Update().push("sentProposals", proposalId);

		mongoOperations.findAndModify(query, update, ProposalsHolder.class);
	}

	@Override
	public void addProposalToNew(String accountId, String proposalId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		Update update = new Update().push("newProposals", proposalId);

		mongoOperations.findAndModify(query, update, ProposalsHolder.class);
	}

	@Override
	public void removeProposalFromSent(String accountId, String proposalId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		Update update = new Update().pull("sentProposals", proposalId);

		mongoOperations.findAndModify(query, update, ProposalsHolder.class);
	}

	@Override
	public void removeProposalFromNew(String accountId, String proposalId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		Update update = new Update().pull("newProposals", proposalId);

		mongoOperations.findAndModify(query, update, ProposalsHolder.class);
	}

	@Override
	public List<String> getNewProposalsByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		query.fields().include("newProposals");

		ProposalsHolder ph = mongoOperations.findOne(query, ProposalsHolder.class);
		return ph.getNewProposals();
	}

	@Override
	public List<String> getSentProposalsByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		query.fields().include("sentProposals");

		ProposalsHolder ph = mongoOperations.findOne(query, ProposalsHolder.class);
		return ph.getSentProposals();
	}

}
