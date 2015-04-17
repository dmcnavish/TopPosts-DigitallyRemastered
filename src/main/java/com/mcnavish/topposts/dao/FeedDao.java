package com.mcnavish.topposts.dao;

import java.util.List;

import com.mcnavish.topposts.hibernate.db.Feeds;

public class FeedDao extends CommonHibernateDao{

	public List<Feeds> listFeeds(){
		return list(Feeds.class);
	}
}
