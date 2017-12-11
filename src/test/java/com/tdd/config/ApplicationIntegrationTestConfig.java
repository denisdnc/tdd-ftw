package com.tdd.config;

import com.github.fakemongo.Fongo;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan(basePackages = {"com.tdd"})
@EnableMongoRepositories({"com.tdd"})
@EnableCaching
public class ApplicationIntegrationTestConfig {

    private static final String DATABASE_NAME = "integration-tests";

    @Bean
    public MongoTemplate mongoTemplate() {
        final MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    public MongoDbFactory mongoDbFactory() {
        final Fongo fongo = new Fongo("mongodb server in memory");
        return new SimpleMongoDbFactory(fongo.getMongo(), DATABASE_NAME);
    }
}
