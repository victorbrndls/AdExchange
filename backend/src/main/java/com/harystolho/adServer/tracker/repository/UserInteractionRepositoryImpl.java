package com.harystolho.adserver.tracker.repository;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adserver.tracker.UserInteraction;

@Service
public class UserInteractionRepositoryImpl implements UserInteractionRepository {

	private MongoOperations mongoOperations;

	private UserInteractionRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public UserInteraction getByInteractorId(String interactorId) {
		Query query = Query.query(Criteria.where("interactorId").is(interactorId));

		return mongoOperations.findOne(query, UserInteraction.class);
	}

	@Override
	public UserInteraction save(UserInteraction userInteraction) {
		return mongoOperations.save(userInteraction);
	}

}
