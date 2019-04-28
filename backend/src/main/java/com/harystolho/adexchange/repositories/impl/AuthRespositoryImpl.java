package com.harystolho.adexchange.repositories.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.AuthRepository;

@Service
public class AuthRespositoryImpl implements AuthRepository {

	private MongoOperations mongoOperations;

	public AuthRespositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	} // b3179c4bbe464e9ab7e7e76aa15fc4d2

	@Override
	public Account save(Account account) {
		return mongoOperations.save(account);
	}

	@Override
	public Account getByEmail(String email) {
		Query query = Query.query(Criteria.where("email").is(email));
		return mongoOperations.findOne(query, Account.class);
	}

}
