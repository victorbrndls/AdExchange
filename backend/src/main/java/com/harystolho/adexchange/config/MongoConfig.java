package com.harystolho.adexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration{

	@Value("${spring.data.mongodb.uri}")
	private String uri;
	
	@Override
	public MongoClient mongoClient() {
		return new MongoClient("localhost", 27017);
	}

	@Override
	protected String getDatabaseName() {
		return "AdExchange";
	}

}
