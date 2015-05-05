package com.mcnavish.topposts.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcnavish.topposts.hibernate.HibernateUtil;
import com.mcnavish.topposts.hibernate.db.CommonHibernate;

public class CommonHibernateDao {

	private static Logger logger = LoggerFactory.getLogger(CommonHibernateDao.class);
	private final SessionFactory sessionFactory;
	private int BATCH_SIZE = 20;
	
	public CommonHibernateDao(){
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public SessionFactory getSessionFactory(){
		return sessionFactory;
	}

	public void save(Object obj){
		Session session = null;
		Transaction transaction = null;
		try{
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(obj);
			transaction.commit();
		}
		catch(Exception ex){
			logger.error("Error executing save", ex);
			if(transaction != null){
				transaction.rollback();
			}
			throw ex;
		}
		finally{
			session.close();
		}
	}
	
	public int saveList(List<? extends Object> objects){
		int totalSaved = 0;
		
		for(Object obj : objects){
			
			try{
				save(obj);
				totalSaved++;
			}
			catch(HibernateException ex){
				logger.error("error inserting record. Continuing");
			}
		}
		
		return totalSaved;
	}
	
	public int batchSaveList(List<? extends Object> objects){
		Session session = null;
		int totalSaved = 0;
		try{
			session = getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			
			for(Object obj : objects){
				
				try{
					session.save(obj);
					transaction.commit();
				}
				catch(HibernateException ex){
					logger.error("error inserting record. Continuing", ex);
					transaction.rollback();
				}
				if( totalSaved % BATCH_SIZE == 0 ){
					session.flush();
					session.clear();
				}
				
				totalSaved++;
			}
		}
		catch(Exception ex){
			logger.error("Error executing saveList", ex);
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
			Transaction transaction = session.beginTransaction();
			result = (List<T>)session.createQuery("from " + cls.getSimpleName()).list();
			transaction.commit();
		}
		catch(Exception ex){
			logger.error("Error executing list", ex);
			throw ex;
		}
		finally{
			session.close();
		}

		return result;
	}
}
