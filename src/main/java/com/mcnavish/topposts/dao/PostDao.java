package com.mcnavish.topposts.dao;

import java.util.List;

import com.mcnavish.topposts.hibernate.HibernateUtil;
import com.mcnavish.topposts.hibernate.db.Posts;

public class PostDao {

	public int savePosts(List<Posts> posts){
		return HibernateUtil.saveList(posts);
	}
}
