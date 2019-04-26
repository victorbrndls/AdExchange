package com.harystolho.adexchange.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

@Service
public class WebsiteRepositoryImpl implements WebsiteRepository {

	private MongoOperations mongoOperations;

	@Autowired
	public WebsiteRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public List<Website> getWebsites() {
		return mongoOperations.findAll(Website.class);
	}

	@Override
	public Pair<RepositoryResponse, Website> saveWebsite(Website website) {
		Website saved = mongoOperations.save(website);

		return Pair.of(RepositoryResponse.CREATED, saved);
	}

	@Override
	public Website getWebsiteById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));

		return mongoOperations.findOne(query, Website.class);
	}

}
