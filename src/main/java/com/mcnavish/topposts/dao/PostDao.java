package com.mcnavish.topposts.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mcnavish.topposts.hibernate.db.Posts;

public class PostDao extends CommonHibernateDao {

	public int savePosts(List<Posts> posts){
		return saveList(posts);
	}
	
	@SuppressWarnings("unchecked")
	public List<Posts> listPosts(DateTime startDate, DateTime endDate){
		Criteria criteria = getSessionFactory().openSession().createCriteria(Posts.class);
		criteria.add( Restrictions.ge("publishedDate", startDate));
		criteria.add( Restrictions.le("publishedDate", endDate));
		return (List<Posts>)criteria.list();
	}
}
