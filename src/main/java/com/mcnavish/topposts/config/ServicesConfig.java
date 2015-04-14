package com.mcnavish.topposts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.scheduler.ProcessFeedsTask;
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
	public PostDao postDao(){
		return new PostDao();
	}
	
	@Bean
	public FeedService feedService(){
		return new FeedService();
	}
	
	@Bean
	public MBeanExporter mBeanExporter(){
		MBeanExporter exporter = new MBeanExporter();
		exporter.setAssembler( mBeanInfoAssembler());
		exporter.setNamingStrategy( metadataNamingStrategy() );
		exporter.setAutodetect(true);
		return exporter;
	}
	
	@Bean
	public AnnotationJmxAttributeSource jmxAttributeSource(){
		AnnotationJmxAttributeSource jmxAttributeSource = new AnnotationJmxAttributeSource();
		return jmxAttributeSource;
	}
	
	@Bean
	public MetadataMBeanInfoAssembler mBeanInfoAssembler(){
		MetadataMBeanInfoAssembler assembler = new MetadataMBeanInfoAssembler();
		assembler.setAttributeSource( jmxAttributeSource() );
		return assembler;
	}
	
	@Bean
	public MetadataNamingStrategy metadataNamingStrategy(){
		MetadataNamingStrategy namingStrategy = new MetadataNamingStrategy();
		namingStrategy.setAttributeSource( jmxAttributeSource() );
		return namingStrategy;
	}
	
}
