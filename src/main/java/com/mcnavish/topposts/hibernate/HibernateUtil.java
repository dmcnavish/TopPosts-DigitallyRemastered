package com.mcnavish.topposts.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.mcnavish.topposts.hibernate.db.CommonHibernate;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory;
	
	static{
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		sessionFactory = configuration.buildSessionFactory(builder.build());
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	public static void save(Object obj){
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(obj);
			session.getTransaction().commit();
		}
		catch(Exception ex){
			System.out.println("Error executing select");
			throw ex;
		}
		finally{
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends CommonHibernate> List<T> list(Class<T> cls){
		List<T> result = null;
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			result = (List<T>)session.createQuery("from Docks").list();
			session.getTransaction().commit();
		}
		catch(Exception ex){
			System.out.println("Error executing select");
			throw ex;
		}
		finally{
			session.close();
		}

		return result;
	}

}
