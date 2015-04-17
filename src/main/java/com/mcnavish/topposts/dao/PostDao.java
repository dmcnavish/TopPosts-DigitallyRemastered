package com.mcnavish.topposts.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mcnavish.topposts.hibernate.db.Posts;

public class PostDao extends CommonHibernateDao {

	public int savePosts(List<Posts> posts){
		return saveList(posts);
	}
	
	public List<Posts> listPosts(){
		
		//TODO: filter by date range and group by max count for feed id
		DateTime endDate = DateTime.now(DateTimeZone.UTC);
		DateTime startDate = endDate.minusDays(1);
		
		Criteria criteria = getSessionFactory().openSession().createCriteria(Posts.class);
		criteria.add( Restrictions.ge("publishedDate", startDate));
		criteria.add( Restrictions.le("publishedDate", endDate));
		return (List<Posts>)criteria.list();
	}
}
