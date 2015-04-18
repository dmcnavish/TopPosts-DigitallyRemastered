package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mcnavish.topposts.domain.Post;

public class TwitterScraper {
	
	private static Logger logger = LoggerFactory.getLogger(RssScraper.class);
	private String TWITTER_COUNT_URL = "http://urls.api.twitter.com/1/urls/count.json?url=";
	
	public void addTwitterTweetCounts(List<Post> posts) throws IOException{
		for(Post post : posts){
			String url = TWITTER_COUNT_URL + post.getUrl();
			int count = getTwitterCount(url);
			post.setTwitterTweets(count);
		}
	}
	
	protected int getTwitterCount(String url) throws IOException, IllegalArgumentException{	
		int count = 0;
		try{
			URL endpoint = new URL(url);
			InputStream input = endpoint.openStream();
			Reader reader = new InputStreamReader(input);
			TwitterResponse response = new Gson().fromJson(reader, TwitterResponse.class);
			if(response != null){
				count = response.getCount();
			}
		
		}catch(IOException ex){
			logger.error("Error getting twitter count for url" + url, ex);
			throw ex;
		}
		catch(Exception ex){
			logger.error("Unhandled error!", ex);
			throw ex;
		}
		
		return count;
	}
	
	protected class TwitterResponse{
		private int count;
		private String url;
		
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
