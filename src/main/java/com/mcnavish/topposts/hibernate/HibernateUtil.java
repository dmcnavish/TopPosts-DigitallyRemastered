package com.mcnavish.topposts.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static final SessionFactory sessionFactory;

	static{
		Configuration configuration = getConfiguration();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		sessionFactory = configuration.buildSessionFactory(builder.build());
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	private static Configuration getConfiguration(){
		boolean useEmbeddedDb = true; //TODO: read from application property
		
		if(useEmbeddedDb){
			logger.debug("Using in memory db configuration");
			return getEmbeddedConfiguration();
		}
		else{
			logger.debug("Using postgresql configuration");
			return getDatabaseConfiguration();
		}
	}

	private static Configuration getDatabaseConfiguration(){
		Configuration configuration = new Configuration().configure();
		
		String connectionHost = System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST");
		String connectionPort = System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT");
		String connectionUrl = "";
		String databaseName = "/topposts";
		
		if(connectionHost == null || connectionHost.isEmpty() || connectionPort == null || connectionPort.isEmpty()){
			connectionHost = "localhost";
			connectionPort = "5432";
		}

		connectionUrl = "jdbc:postgresql://" + connectionHost + ":" + connectionPort + databaseName;
		
		String connectionUserName = System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME");
		String connectionPassword  = System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD");
		
		if(connectionUserName == null || connectionUserName.isEmpty() || connectionPassword == null || connectionPassword.isEmpty()){
			connectionUserName = "davidmcnavish";
			connectionPassword = "";
		}
		
		logger.debug("connectionUrl: " + connectionUrl);
		logger.debug("connectionUserName: " + connectionUserName);
		logger.debug("connectionPassword: " + connectionPassword);

		configuration.setProperty("hibernate.connection.url", connectionUrl);
		configuration.setProperty("hibernate.connection.username", connectionUserName);
		configuration.setProperty("hibernate.connection.password", connectionPassword);
		configuration.setProperty(Environment.DRIVER, "org.postgresql.Driver");
		
		
		return configuration;
	}
	
	private static Configuration getEmbeddedConfiguration(){
		Configuration configuration = new Configuration().configure();
		
		String connectionUrl = "jdbc:h2:~/mem;MODE=PostgreSQL;INIT=RUNSCRIPT FROM 'classpath:queries.sql';DATABASE_TO_UPPER=false";
		configuration.setProperty("hibernate.connection.url", connectionUrl);
		configuration.setProperty(Environment.DRIVER, "org.h2.Driver");
		
		return configuration;
	}
	
}
