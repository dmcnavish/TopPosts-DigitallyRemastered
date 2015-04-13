package com.mcnavish.topposts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.services.FeedService;

@Configuration
public class ServicesConfig {

	@Bean
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/davidmcnavish");
		dataSource.setUsername("davidmcnavish");
		return dataSource;
	}
	
	@Bean
	public PostDao getPostDao(){
		return new PostDao();
	}
	
	@Bean
	public FeedService getFeedService(){
		return new FeedService();
	}
	
}
