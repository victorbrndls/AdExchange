package com.harystolho.adexchange.repositories.userData;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.models.UserData;

/**
 * Make sure the {@link UserData} exists in the database before inserting
 * something in it
 * 
 * @author Harystolho
 *
 */
@Service
public class UserDataRepositoryImpl implements UserDataRepository {

	private MongoOperations mongoOperations;

	public UserDataRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public UserData save(UserData userData) {
		return mongoOperations.save(userData);
	}

	@Override
	public ProposalsHolder saveProposalsHolder(ProposalsHolder ph) {
		Query query = Query.query(Criteria.where("accountId").is(ph.getAccountId()));
		Update update = new Update().set("proposalsHolder", ph);

		UserData ud = mongoOperations.findAndModify(query, update, UserData.class);
		return ud.getProposalsHolder();
	}

	@Override
	public ProposalsHolder getProposalsHolderByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		query.fields().include("proposalsHolder");

		UserData ud = mongoOperations.findOne(query, UserData.class);
		return ud.getProposalsHolder();
	}

	@Override
	public List<String> saveContractsByAccountId(String accountId, List<String> contracts) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		Update update = new Update().set("contracts", contracts);

		UserData ud = mongoOperations.findAndModify(query, update, UserData.class);
		return ud.getContracts();
	}

	@Override
	public List<String> getContractsByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		query.fields().include("contracts");

		UserData ud = mongoOperations.findOne(query, UserData.class);
		return ud.getContracts();
	}

	@Override
	public boolean exists(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		return mongoOperations.exists(query, UserData.class);
	}

}
