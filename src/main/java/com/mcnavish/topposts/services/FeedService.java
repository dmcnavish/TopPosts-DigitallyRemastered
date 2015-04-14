package com.mcnavish.topposts.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.domain.DomainObjectConversion;
import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.hibernate.db.Posts;
import com.mcnavish.topposts.scraper.RssScraper;


public class FeedService {
	
	@Autowired
	private PostDao postDao;
	
	private static Logger logger = LoggerFactory.getLogger(FeedService.class);

	public void processFeeds(){
		
		//TODO:get feeds from db
		
		List<Feed> feeds = new ArrayList<Feed>();
		
		Feed feed = new Feed();
		feed.setFeedId(1);
		feed.setUrl("http://feeds.gawker.com/gizmodo/full");
		feed.setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		feeds.add(feed);
		
		DateTime minimumDate = DateTime.now();
		minimumDate.minusHours( minimumDate.getHourOfDay());
		minimumDate.minusMinutes( minimumDate.getMinuteOfHour());
		
		RssScraper rssScrapper = new RssScraper();
		List<Post> posts = rssScrapper.getAllPosts(feeds, minimumDate);
		
		if(posts == null || posts.isEmpty()){
			logger.debug("No posts found for day :" + minimumDate);
			return;
		}
		logger.debug("Total posts to save: " + posts.size());
		List<Posts> allPosts = DomainObjectConversion.toPosts(posts);
		int totalSaved = postDao.savePosts(allPosts);
		logger.debug(totalSaved + " Posts saved.");
	}
}
