package com.mcnavish.topposts.controllers;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.scraper.IHeartQuotesScraper;
import com.mcnavish.topposts.services.FeedService;
import com.mcnavish.topposts.services.PostService;

@Controller
public class ViewController {

	private static Logger logger = LoggerFactory.getLogger(ViewController.class);
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private PostService postService;
	
	private DateTimeFormatter requestDateFormatter = DateTimeFormat.forPattern("MMddyyyy");
	
	@RequestMapping("/")
	public ModelAndView index(@RequestParam(value="date", required=false) String date, Map<String, Object> model) throws Exception{	
		DateTime endDate = DateTime.now(DateTimeZone.UTC);
		if(date != null){
			endDate = requestDateFormatter.withZoneUTC().parseDateTime(date);
			if(endDate.dayOfYear() == DateTime.now().dayOfYear()){
				endDate = DateTime.now(DateTimeZone.UTC);
			}
		}
		DateTime startDate = endDate.minusDays(1).withTimeAtStartOfDay();
		
		List<Post> posts = postService.listTopPosts(startDate, endDate);
		
		String quote = IHeartQuotesScraper.getQuote();
		
		ModelAndView mav = new ModelAndView("layout");
		mav.addObject("posts", posts);
		mav.addObject("quote", quote);
		

		logger.debug("Total posts found: " + posts.size());
		return mav;
	}
	
	@RequestMapping("/process")
	public String processFeeds() throws Exception{
		
		feedService.processFeeds();
		
		return "success";
	}
	
}
