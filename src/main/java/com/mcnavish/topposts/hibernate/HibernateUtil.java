package com.mcnavish.topposts.hibernate;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class HibernateUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static final SessionFactory sessionFactory;
	
//	@Autowired
//	private static DataSource dataSource;
	
	static{
		Configuration configuration = new Configuration().configure();
		String connectionHost = System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST");
		String connectionPort = System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT");
		String databaseName = "/topposts";
		boolean isOpenShift = true;
		
		if(connectionHost == null || connectionHost.isEmpty() || connectionPort == null || connectionPort.isEmpty()){
			connectionHost = "localhost";
			connectionPort = "5432";
			isOpenShift = false;
		}

		String connectionUrl = "jdbc:postgresql://" + connectionHost + ":" + connectionPort + databaseName;
		
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
		
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		
		if(isOpenShift){
			try{
				DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/PostgreSQLDS");
				builder.applySetting(Environment.DATASOURCE, dataSource);
			}catch(Exception ex){
				logger.error("Error setting datasource", ex);
			}
		}
		
		
		sessionFactory = configuration.buildSessionFactory(builder.build());
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
}
