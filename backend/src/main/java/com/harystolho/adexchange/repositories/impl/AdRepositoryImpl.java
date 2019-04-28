package com.harystolho.adexchange.repositories.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.repositories.AdRepository;

@Service
public class AdRepositoryImpl implements AdRepository {

	private MongoOperations mongoOperations;

	public AdRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public Ad save(Ad ad) {
		return mongoOperations.save(ad);
	}

	@Override
	public List<Ad> getAdsByAccountId(String accountId) {
		Query query = Query.query(Criteria.where("accountId").is(accountId));
		return mongoOperations.find(query, Ad.class);
	}

	@Override
	public Ad getAdById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));
		return mongoOperations.findOne(query, Ad.class);
	}

}
