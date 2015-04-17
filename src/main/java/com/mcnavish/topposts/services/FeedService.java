package com.mcnavish.topposts.services;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mcnavish.topposts.dao.FeedDao;
import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.domain.DomainObjectConversion;
import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.hibernate.db.Feeds;
import com.mcnavish.topposts.hibernate.db.Posts;
import com.mcnavish.topposts.scraper.FacebookScraper;
import com.mcnavish.topposts.scraper.RssScraper;


public class FeedService {
	
	@Autowired
	private PostDao postDao;
	@Autowired
	private FeedDao feedDao;
	
	private static Logger logger = LoggerFactory.getLogger(FeedService.class);

	public void processFeeds() throws Exception{
		List<Feeds> feeds = feedDao.listFeeds();
		if(feeds == null || feeds.isEmpty()){
			logger.debug("No feeds found!");
			return;
		}
		List<Feed> allFeeds = DomainObjectConversion.toListFeed(feeds);
		
		DateTime minimumDate = DateTime.now();
		minimumDate.minusHours( minimumDate.getHourOfDay());
		minimumDate.minusMinutes( minimumDate.getMinuteOfHour());
		
		RssScraper rssScrapper = new RssScraper();
		List<Post> posts = rssScrapper.getAllPosts(allFeeds, minimumDate);
		
		if(posts == null || posts.isEmpty()){
			logger.debug("No posts found for day :" + minimumDate);
			return;
		}
		
		FacebookScraper facebookScraper = new FacebookScraper();
		facebookScraper.addFacebookLikeCounts(posts);
		
		//TODO: get likes and tweets for all posts
		
		//TODO: filter out only the top posts for each feed 
		
		logger.debug("Total posts to save: " + posts.size());
		List<Posts> allPosts = DomainObjectConversion.toDbPosts(posts);
		int totalSaved = postDao.savePosts(allPosts);
		logger.debug(totalSaved + " Posts saved.");
	}
}
