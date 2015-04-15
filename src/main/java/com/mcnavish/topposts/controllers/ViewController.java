package com.mcnavish.topposts.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.services.FeedService;
import com.mcnavish.topposts.services.PostService;

@Controller
public class ViewController {

	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping("/")
	public String index(Map<String, Object> model) throws Exception{	
		
		List<Post> posts = postService.listTopPosts();
		
		model.put("posts", posts);
		return "index";
	}
	
	@RequestMapping("/process")
	public String processFeeds() throws Exception{
		
		feedService.processFeeds();
		
		return "success";
	}
	
}
