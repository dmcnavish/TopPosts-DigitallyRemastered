package com.mcnavish.topposts.scraper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mcnavish.topposts.domain.Post;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class FacebookScraperTest {

	private FacebookScraper facebookScraper;
	
	@Before
	public void setup(){
		facebookScraper = new FacebookScraper();
	}
	
	@Test
	public void buildFacebookLikeUrlTest(){
		
		String expectedUrl1 = "http://gizmodo.com/this-mad-genius-built-his-own-game-streaming-server-for-1697885636";
		String expectedUrl2 = "http://gizmodo.com/spacex-crashed-a-rocket-on-a-drone-barge-again-1697530848/1697892830/+chris-mills";
		String expectedUrl3 = "http://gizmodo.com/heres-a-tank-drifting-across-a-frozen-lake-1697883111";
		
		List<Post> posts = new ArrayList<Post>();
		Post post1 = new Post();
		post1.setUrl(expectedUrl1);
		posts.add(post1);
		
		Post post2 = new Post();
		post2.setUrl(expectedUrl2);
		posts.add(post2);
		
		Post post3 = new Post();
		post3.setUrl(expectedUrl3);
		posts.add(post3);
		
		String resultUrl = facebookScraper.buildFacebookLikeUrl(posts);
		
		String expectedResult = facebookScraper.FACEBOOK_LIKE_URL + expectedUrl1 + "\",\"" + expectedUrl2 + "\",\"" + expectedUrl3 + "\")";
		Assert.assertEquals(expectedResult, resultUrl);
	}
	
	@Test
	public void getAndAddCountsTest() throws Exception{
		
		String testUrl = "http://mcnavish.com";
		String expectedUrl1 = "http://gizmodo.com/this-mad-genius-built-his-own-game-streaming-server-for-1697885636";
		int expectedTotal1 = 101;
		String expectedUrl2 = "http://gizmodo.com/spacex-crashed-a-rocket-on-a-drone-barge-again-1697530848/1697892830/+chris-mills";
		int expectedTotal2 = 202;
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		p1.setUrl(expectedUrl1);
		posts.add(p1);
		Post p2 = new Post();
		p2.setUrl(expectedUrl2);
		posts.add(p2);
		
		String responseWithItems = "<fql_query_response list=\"true\" xmlns=\"http://api.facebook.com/1.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"<link_stat>\n" + 
				"<url>" + expectedUrl1 + "</url>\n" + 
				"<click_count>0</click_count>\n" + 
				"<like_count>61</like_count>\n" + 
				"<share_count>23</share_count>\n" + 
				"<total_count>" + expectedTotal1 + "</total_count>\n" + 
				"</link_stat>\n" + 
				"<link_stat>\n" + 
				"<url>" + expectedUrl2 + "</url>\n" + 
				"<click_count>0</click_count>\n" + 
				"<like_count>0</like_count>\n" + 
				"<share_count>0</share_count>\n" + 
				"<total_count>" + expectedTotal2 + "</total_count>\n" + 
				"</link_stat>\n" + 
				"</fql_query_response>";
				
		URL requestUrl = new URL(testUrl);
		Document doc = Jsoup.parseBodyFragment(responseWithItems);
		
		//mocks
		PowerMockito.spy(Jsoup.class);
		PowerMockito.doReturn( doc ).when(Jsoup.class, "parse", requestUrl, 3000);
		
		facebookScraper.getAndAddCounts(posts, testUrl);
		
		Assert.assertEquals(2, posts.size());
		Assert.assertEquals(expectedTotal1, posts.get(0).getFaceBookLikes());
		Assert.assertEquals(expectedTotal2, posts.get(1).getFaceBookLikes());
	}
	
	@Test
	public void getAndAddCountsTest_noItems() throws Exception{
		
		String testUrl = "http://mcnavish.com";
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		posts.add(p1);
		Post p2 = new Post();
		posts.add(p2);
		
		String responseWithItems = "<fql_query_response list=\"true\" xmlns=\"http://api.facebook.com/1.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"</fql_query_response>";
				
		URL requestUrl = new URL(testUrl);
		Document doc = Jsoup.parseBodyFragment(responseWithItems);
		
		//mocks
		PowerMockito.spy(Jsoup.class);
		PowerMockito.doReturn( doc ).when(Jsoup.class, "parse", requestUrl, 3000);
		
		facebookScraper.getAndAddCounts(posts, testUrl);
		
		Assert.assertEquals(2, posts.size());
		Assert.assertEquals(0, posts.get(0).getFaceBookLikes());
		Assert.assertEquals(0, posts.get(1).getFaceBookLikes());
	}
	
	@Test
	public void getAndAddCountsTest_error() throws Exception{
		
		String testUrl = "http://mcnavish.com";
		List<Post> posts = new ArrayList<Post>();
		Post p1 = new Post();
		posts.add(p1);
		Post p2 = new Post();
		posts.add(p2);
		
		String responseWithItems = "invalid response";
				
		URL requestUrl = new URL(testUrl);
		Document doc = Jsoup.parseBodyFragment(responseWithItems);
		
		//mocks
		PowerMockito.spy(Jsoup.class);
		PowerMockito.doReturn( doc ).when(Jsoup.class, "parse", requestUrl, 3000);
		
		facebookScraper.getAndAddCounts(posts, testUrl);
		
		Assert.assertEquals(2, posts.size());
		Assert.assertEquals(0, posts.get(0).getFaceBookLikes());
		Assert.assertEquals(0, posts.get(1).getFaceBookLikes());
	}
}
