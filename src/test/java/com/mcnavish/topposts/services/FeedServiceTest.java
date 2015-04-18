package com.mcnavish.topposts.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;


public class FeedServiceTest {

	private FeedService feedService;
	
	@Before
	public void setup(){
		feedService = new FeedService();
	}

	@Test
	public void getTopPostsTest_singleFeedSinglePost(){
		String expectedTitle = "title1";
		
		Feed feed1 = new Feed();
		feed1.setFeedId(1);
		
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		p1.setFaceBookLikes(1);
		p1.setTwitterTweets(2);
		p1.setTitle(expectedTitle);
		p1.setFeed(feed1);
		posts.add(p1);
		
		List<Post>  result = feedService.getTopPosts(posts);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expectedTitle, result.get(0).getTitle());
	}
	
	@Test
	public void getTopPostsTest_singleFeed(){
		String expectedTitle = "title2";
		
		Feed feed1 = new Feed();
		feed1.setFeedId(1);
		
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		p1.setFaceBookLikes(1);
		p1.setTwitterTweets(2);
		p1.setTitle("title1");
		p1.setFeed(feed1);
		posts.add(p1);
		
		Post p2 = new Post();
		p2.setFaceBookLikes(6);
		p2.setTwitterTweets(5);
		p2.setTitle(expectedTitle);
		p2.setFeed(feed1);
		posts.add(p2);
		
		List<Post>  result = feedService.getTopPosts(posts);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expectedTitle, result.get(0).getTitle());
	}
	
	@Test
	public void getTopPostsTest_multipleFeeds(){
		String expectedTitle1 = "expectedTitle1";
		String expectedTitle2 = "expectedTitle2";
		
		Feed feed1 = new Feed();
		feed1.setFeedId(1);
		
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		p1.setFaceBookLikes(1);
		p1.setTwitterTweets(2);
		p1.setTitle("title1");
		p1.setFeed(feed1);
		posts.add(p1);
		
		Post p2 = new Post();
		p2.setFaceBookLikes(6);
		p2.setTwitterTweets(5);
		p2.setTitle(expectedTitle1);
		p2.setFeed(feed1);
		posts.add(p2);
		
		Feed feed2 = new Feed();
		feed2.setFeedId(2);
		
		Post p12 = new Post();
		p12.setFaceBookLikes(11);
		p12.setTwitterTweets(22);
		p12.setTitle(expectedTitle2);
		p12.setFeed(feed2);
		posts.add(p12);
		
		Post p22 = new Post();
		p22.setFaceBookLikes(16);
		p22.setTwitterTweets(5);
		p22.setTitle("wrong");
		p22.setFeed(feed2);
		posts.add(p22);
		
		List<Post>  result = feedService.getTopPosts(posts);
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expectedTitle1, result.get(0).getTitle());
		Assert.assertEquals(expectedTitle2, result.get(1).getTitle());
	}
}
