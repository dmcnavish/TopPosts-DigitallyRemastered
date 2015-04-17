package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcnavish.topposts.domain.Post;
import com.mcnavish.topposts.services.FeedService;

public class FacebookScraper {
	
	private static Logger logger = LoggerFactory.getLogger(FeedService.class);
	
	private int REQUEST_TIMEOUT_MILLIS = 3000;
	protected String FACEBOOK_LIKE_URL = "http://api.facebook.com/method/fql.query?query=select%20%20url,click_count,like_count,%20share_count,total_count%20from%20link_stat%20where%20url%20IN%20(\"";
	protected int FACEBOOK_URL_SIZE = 5;

	public void addFacebookLikeCounts(List<Post> posts) throws IOException, IllegalArgumentException{
		for(int i=0; i< posts.size(); i+=FACEBOOK_URL_SIZE){
			List<Post> subListPosts = posts.subList(i, i+FACEBOOK_URL_SIZE);
			String url = buildFacebookLikeUrl(subListPosts);
			getAndAddCounts(subListPosts, url);
		}
	}
	
	protected String buildFacebookLikeUrl(List<Post> posts){
		String url = FACEBOOK_LIKE_URL;
		url += posts.stream().map(Post::getUrl).reduce( (r, u) -> r + "\",\"" + u).get();
		url += "\")";
		logger.debug("fb url: " + url);
		return url;
	}
	
	protected void getAndAddCounts(List<Post> posts, String url) throws IOException, IllegalArgumentException{	
		try{
			URL requestUrl = new URL(url);
			Document doc = Jsoup.parse(requestUrl, REQUEST_TIMEOUT_MILLIS);
			Elements response = doc.select("fql_query_response");
			if(response == null || response.size() == 0){
				logger.error("channel not found in fb for url: " + url);
				return;
			}
			
			Elements items = response.first().select("link_stat");
			for(Element item : items){
				for(Post post : posts){
					if(post.getUrl().equalsIgnoreCase( item.select("url").text() )){
						post.setFaceBookLikes( Integer.parseInt( item.select("total_count").text() ) );
						break;
					}
				}
			}
		
		}catch(IOException | IllegalArgumentException ex){
			logger.error("Error getting posts", ex);
			throw ex;
		}
		catch(Exception ex){
			logger.error("Unhandled error!", ex);
			throw ex;
		}
	}
	
}
