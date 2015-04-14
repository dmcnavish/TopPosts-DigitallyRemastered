package com.mcnavish.topposts.dao;

import java.util.List;

import com.mcnavish.topposts.hibernate.HibernateUtil;
import com.mcnavish.topposts.hibernate.db.Feeds;

public class FeedDao {

	public List<Feeds> listFeeds(){
		return HibernateUtil.list(Feeds.class);
	}
}
