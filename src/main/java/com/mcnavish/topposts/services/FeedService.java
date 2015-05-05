package com.mcnavish.topposts.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mcnavish.topposts.dao.FeedDao;
import com.mcnavish.topposts.dao.PostDao;
import com.mcnavish.topposts.domain.DomainObjectConversion;
import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.domain.PostRatingDecreasingComparator;
import com.mcnavish.topposts.hibernate.db.Feeds;
import com.mcnavish.topposts.hibernate.db.Posts;
import com.mcnavish.topposts.scraper.FacebookScraper;
import com.mcnavish.topposts.scraper.RssScraper;
import com.mcnavish.topposts.scraper.TwitterScraper;


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
		
		DateTime minimumDate = DateTime.now().minusDays(1).withTimeAtStartOfDay();
		logger.debug("Getting posts with minimumDate: " + minimumDate);
		 
		RssScraper rssScrapper = new RssScraper();
		List<Post> posts = rssScrapper.getAllPosts(allFeeds, minimumDate);
		
		if(posts == null || posts.isEmpty()){
			logger.debug("No posts found for day :" + minimumDate);
			return;
		}
		
		FacebookScraper facebookScraper = new FacebookScraper();
		facebookScraper.addFacebookLikeCounts(posts);
		
		TwitterScraper twitterScraper = new TwitterScraper();
		twitterScraper.addTwitterTweetCounts(posts);
		
		List<Post> topPosts = getTopPosts(posts);
		
		logger.debug("Total posts to save: " + topPosts.size());
		List<Posts> postsToSave = DomainObjectConversion.toDbPosts(topPosts);
		int totalSaved = postDao.savePosts(postsToSave);
		logger.debug(totalSaved + " Posts saved.");
	}
	
	protected List<Post> getTopPosts(List<Post> posts){
		List<Post> topPosts = new ArrayList<Post>();
		Map<Long, List<Post>> feedPosts = posts.stream().collect( Collectors.groupingBy( Post::getFeedId ));
		
		for(long key : feedPosts.keySet()){
			List<Post> singleFeedPosts = feedPosts.get(key);
			Collections.sort( singleFeedPosts, new PostRatingDecreasingComparator() );
			topPosts.add( singleFeedPosts.get(0));
		}
		
		return topPosts;
		
	}
}
