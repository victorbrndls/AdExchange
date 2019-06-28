package com.harystolho.adexchange.admin;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.controllers.models.BalanceWithdrawModel;

@Service
public class AdminRepositoryImpl implements AdminRepository {

	private MongoOperations mongoOperations;

	private AdminRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void saveBalanceWithdrawRequest(BalanceWithdrawModel model) {
		mongoOperations.save(model, "admin");
	}

}
