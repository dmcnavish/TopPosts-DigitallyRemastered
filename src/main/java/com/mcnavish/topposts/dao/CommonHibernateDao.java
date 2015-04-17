package com.mcnavish.topposts.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mcnavish.topposts.hibernate.HibernateUtil;
import com.mcnavish.topposts.hibernate.db.CommonHibernate;

public class CommonHibernateDao {

	private final SessionFactory sessionFactory;
	
	private int BATCH_SIZE = 20;
	
	public CommonHibernateDao(){
//		Configuration configuration = new Configuration().configure();
//		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//		sessionFactory = configuration.buildSessionFactory(builder.build());
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public SessionFactory getSessionFactory(){
		return sessionFactory;
	}

	public void save(Object obj){
		Session session = null;
		try{
			session = getSessionFactory().openSession();
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
	
	public int saveList(List<? extends Object> objects){
		Session session = null;
		int totalSaved = 0;
		try{
			session = getSessionFactory().openSession();
			session.beginTransaction();
			
			for(Object obj : objects){
				session.save(obj);
				if( totalSaved % BATCH_SIZE == 0 ){
					session.flush();
					session.clear();
				}
				
				totalSaved++;
			}
			
			session.getTransaction().commit();
		}
		catch(Exception ex){
			System.out.println("Error executing select");
			throw ex;
		}
		finally{
			session.close();
		}
		
		return totalSaved;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CommonHibernate> List<T> list(Class<T> cls){
		List<T> result = null;
		Session session = null;
		try{
			session = getSessionFactory().openSession();
			session.beginTransaction();
			result = (List<T>)session.createQuery("from " + cls.getSimpleName()).list();
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
