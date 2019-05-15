package com.harystolho.adexchange.repositories.account;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Account;

@Service(value="accountRepository")
public class AccountRespositoryImpl implements AccountRepository {

	private MongoOperations mongoOperations;

	public AccountRespositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Account save(Account account) {
		return mongoOperations.save(account);
	}

	@Override
	public Account getByEmail(String email) {
		Query query = Query.query(Criteria.where("email").is(email));
		return mongoOperations.findOne(query, Account.class);
	}

	@Override
	public Account getById(String accountId) {
		Query query = Query.query(Criteria.where("_id").is(accountId));
		return mongoOperations.findOne(query, Account.class);
	}

}
