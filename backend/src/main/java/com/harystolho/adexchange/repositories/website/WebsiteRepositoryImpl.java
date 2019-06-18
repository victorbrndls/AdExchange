package com.harystolho.adexchange.repositories.website;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Website;

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
	public List<Website> getWebsites(Set<String> filters) {
		Query query = Query.query(Criteria.where("categories").in(filters));

		return mongoOperations.find(query, Website.class);
	}

	@Override
	public Website save(Website website) {
		Website saved = mongoOperations.save(website);

		return saved;
	}

	@Override
	public Website getById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));

		return mongoOperations.findOne(query, Website.class);
	}

	@Override
	public void deleteById(String id) {
		Query query = Query.query(Criteria.where("_id").is(id));

		mongoOperations.remove(query, Website.class);
	}

}
