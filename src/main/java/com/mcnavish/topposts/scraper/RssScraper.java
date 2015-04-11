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

import com.mcnavish.topposts.domain.Post;

public class RssScraper {
	
	private static Logger logger = LoggerFactory.getLogger(RssScraper.class);
	
	private int REQUEST_TIMEOUT_MILLIS = 3000;
	private DateTimeFormatter FeedBurnerDateFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz");  //Mon, 06 Apr 2015 04:00:00 GMT

	public List<Post> getPosts(String feedUrl, DateTime minimumDate){
		List<Post> allPosts = new ArrayList<Post>();
		try{
			String url = buildYqlUrl(feedUrl);
			allPosts = extractPosts(url, minimumDate);
		}
		catch(IOException ex){
			//TODO: mark feed status as 'error'
		}
		
		return allPosts;
	}
	
	private String buildYqlUrl(String feedUrl){
		String newUrl = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20feed%20where%20url=\"" + feedUrl + "\"";
		return newUrl;
	}

	
	protected List<Post> extractPosts(String url, DateTime minimumDate) throws IOException{
		List<Post> allPosts = new ArrayList<Post>();
		
		try{
			URL requestUrl = new URL(url);
			Document doc = Jsoup.parse(requestUrl, REQUEST_TIMEOUT_MILLIS);
			
			Elements channel = doc.select("channel");
			if(channel == null || channel.size() == 0){
				logger.error("channel not found in feed for url: " + url);
				return allPosts;
			}
			Elements items = channel.first().select("item");
			for(Element item : items){
				logger.debug(item.select("title").text() + " pubDate: " + item.select("pubDate").text());				
				Post post = new Post();
				post.setTitle(item.select("title").text());
				post.setLink(item.select("link").text());
				post.setHtml(item.select("description").text());
				String pubDate = item.select("pubDate").text();
				DateTime dt = FeedBurnerDateFormatter.parseDateTime(pubDate);
				post.setPublishedDate(dt);
				allPosts.add(post);
			}
			
		}
		catch(Exception ex){
			logger.error("Error getting posts", ex);
			throw new IOException("Error getting posts", ex);
		}
		
		return allPosts;
	}
	
	
}
