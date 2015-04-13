package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;

public class RssScraper {
	
	private static Logger logger = LoggerFactory.getLogger(RssScraper.class);
	
	private int REQUEST_TIMEOUT_MILLIS = 3000;
	protected String YQL_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20feed%20where%20url=\"[[FEED_URL]]\"";

	public List<Post> getAllPosts(List<Feed> feeds, DateTime minimumDate){
		List<Post> posts = new ArrayList<Post>();
		for(Feed feed : feeds){
			posts.addAll(getPosts(feed, minimumDate));
		}
		
		return posts;
	}
	
	public List<Post> getPosts(Feed feed, DateTime minimumDate){
		List<Post> allPosts = new ArrayList<Post>();
		try{
			allPosts = extractPosts(feed, minimumDate);
		}
		catch(Exception ex){
			//TODO: mark feed status as 'error'
		}
		
		return allPosts;
	}
	
	protected String buildYqlUrl(String feedUrl){
		String newUrl = YQL_URL.replace("[[FEED_URL]]", feedUrl);
		return newUrl;
	}

	
	protected List<Post> extractPosts(Feed feed, DateTime minimumDate) throws IOException, IllegalArgumentException{
		List<Post> allPosts = new ArrayList<Post>();
		
		try{
			String url = buildYqlUrl( feed.getUrl());
			URL requestUrl = new URL(url);
			Document doc = Jsoup.parse(requestUrl, REQUEST_TIMEOUT_MILLIS);
			Elements channel = doc.select("results");
			if(channel == null || channel.size() == 0){
				logger.error("channel not found in feed for url: " + url);
				return allPosts;
			}
			
			Elements items = channel.first().select("item");
			if(items.isEmpty()){
				logger.error("no items found in feed for url: " + url);
				return allPosts;
			}
			DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(feed.getDateFormat());
			
			for(Element item : items){
				logger.debug(item.select("title").text() + " pubDate: " + item.select("pubDate").text());				
				Post post = new Post();
				post.setTitle(item.select("title").text());
				post.setUrl(item.select("feedburner|origLink").text());
				post.setHtml(item.select("description").text());
				String pubDate = item.select("pubDate").text();
				DateTime dt = dateFormatter.parseDateTime(pubDate);
				post.setPublishedDate(dt);
				post.setFeed(feed);
				allPosts.add(post);
			}
			
		}
		catch(IOException | IllegalArgumentException ex){
			logger.error("Error getting posts", ex);
			throw ex;
		}
		
		return allPosts;
	}
	
}
