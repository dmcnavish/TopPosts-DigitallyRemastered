package com.mcnavish.topposts.controllers;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.services.FeedService;
import com.mcnavish.topposts.services.PostService;

@Controller
public class ViewController {

	private static Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private PostService postService;
	
	@RequestMapping("/")
	public ModelAndView index(Map<String, Object> model) throws Exception{	
		//TODO: get dates from request
		DateTime endDate = DateTime.now(DateTimeZone.UTC);
		DateTime startDate = endDate.minusDays(1);
		
		List<Post> posts = postService.listTopPosts(startDate, endDate);

		ModelAndView mav = new ModelAndView("layout");
		mav.addObject("posts", posts);

		logger.debug("Total posts found: " + posts.size());
		return mav;
	}
	
	@RequestMapping("/process")
	public String processFeeds() throws Exception{
		
		feedService.processFeeds();
		
		return "success";
	}
	
}
