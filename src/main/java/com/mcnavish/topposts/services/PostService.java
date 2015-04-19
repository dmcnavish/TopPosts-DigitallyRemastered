package com.mcnavish.topposts.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.domain.DomainObjectConversion;
import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.hibernate.db.Posts;

public class PostService {

	@Autowired
	private PostDao postDao;
	
	public List<Post> listTopPosts(DateTime startDate, DateTime endDate){
		List<Post> allPosts = new ArrayList<Post>();
		
		List<Posts> posts = postDao.listPosts(startDate, endDate);
		if(posts == null) return allPosts;
		
		allPosts = DomainObjectConversion.toPost(posts);
		return allPosts;
	}
}
