package com.harystolho.adexchange.repositories.spot;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Spot;

@Service
public class SpotRepositoryImpl implements SpotRepository {

	private MongoOperations mongoOperations;

	private SpotRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Spot save(Spot spot) {
		return mongoOperations.save(spot);
	}

	@Override
	public Spot getById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));
		return mongoOperations.findOne(query, Spot.class);
	}

}
