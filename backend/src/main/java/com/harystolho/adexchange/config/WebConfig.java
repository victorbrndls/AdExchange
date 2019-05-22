package com.harystolho.adexchange.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.harystolho.adexchange.converters.AdReaderConverter;
import com.harystolho.adexchange.converters.NotificationReaderConverter;

@Configuration
@EnableScheduling
public class WebConfig {

	private MongoDbFactory mongoDbFactory;

	@Autowired
	public WebConfig(MongoDbFactory mongoDbFactory) {
		this.mongoDbFactory = mongoDbFactory;
	}

	@Bean
	public MongoCustomConversions customConversions() {
		final List<Converter<?, ?>> converters = new ArrayList<>();

		converters.add(new AdReaderConverter());
		converters.add(new NotificationReaderConverter());

		return new MongoCustomConversions(converters);
	}

	@Bean
	public MappingMongoConverter mongoConverter() throws Exception {
		MongoMappingContext mappingContext = new MongoMappingContext();
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);

		MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
		mongoConverter.setCustomConversions(customConversions());

		// This removes the _class field in the document that is added by default
		mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		mongoConverter.afterPropertiesSet();

		return mongoConverter;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory, mongoConverter());
	}

	@Bean("ae_taskExecutor")
	public TaskExecutor threatPoolTaskExecutor() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();

		te.setCorePoolSize(2);
		te.setMaxPoolSize(8);
		te.setThreadNamePrefix("task_executor_bean");
		te.initialize();

		return te;
	}

}
