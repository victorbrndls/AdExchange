package com.harystolho.adexchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class WebConfig {

	private MongoDbFactory mongoDbFactory;

	@Autowired
	public WebConfig(MongoDbFactory mongoDbFactory) {
		this.mongoDbFactory = mongoDbFactory;
	}

	@Bean
	public MappingMongoConverter mongoConverter() throws Exception {
		MongoMappingContext mappingContext = new MongoMappingContext();
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);

		MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
		
		// This removes the _class field in the document that is added by default
		mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		mongoConverter.afterPropertiesSet();

		return mongoConverter;
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory, mongoConverter());
	}

}
