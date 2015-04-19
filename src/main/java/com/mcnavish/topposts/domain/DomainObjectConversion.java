package com.mcnavish.topposts.domain;

import java.util.ArrayList;
import java.util.List;

import com.mcnavish.topposts.hibernate.db.Feeds;
import com.mcnavish.topposts.hibernate.db.Posts;


public class DomainObjectConversion {
	
	public static List<Post> toPost(List<Posts> posts){
		List<Post> allPosts = new ArrayList<Post>();
		
		for(Posts post : posts){
			allPosts.add( toPost(post));
		}
		
		return allPosts;
	}
	
	public static Post toPost(Posts posts){
		Post post = new Post();
		post.setFaceBookLikes( posts.getFacebookLikes());
		if(posts.getHtml() != null){
			post.setHtml( new String(posts.getHtml()) );
		}
		post.setUrl( posts.getUrl());
		post.setPublishedDate( posts.getPublishedDate());
		post.setTitle(posts.getTitle());
		post.setTwitterTweets( posts.getTwitterTweets());
		post.setFeed( toFeed( posts.getFeeds() ));
		return post;
	}
	
	public static List<Posts> toDbPosts(List<Post> allPosts){
		List<Posts> posts = new ArrayList<Posts>();
		
		for(Post post : allPosts){
			posts.add( toDbPosts(post));
		}
		
		return posts;
	}

	public static Posts toDbPosts(Post post){
		Posts posts = new Posts();
		posts.setFacebookLikes( post.getFaceBookLikes());
		if(post.getHtml() != null){
			posts.setHtml( post.getHtml().getBytes() );
		}
		posts.setUrl( post.getUrl());
		posts.setPublishedDate( post.getPublishedDate());
		posts.setTitle(post.getTitle());
		posts.setTwitterTweets( post.getTwitterTweets());
		posts.setFeeds( toDbFeeds( post.getFeed() ));
		return posts;
	}
	
	public static List<Feed> toListFeed(List<Feeds> feeds){
		List<Feed> allFeeds = new ArrayList<Feed>();
		
		for(Feeds feed : feeds){
			allFeeds.add( toFeed(feed));
		}
		
		return allFeeds;
	}
	
	public static Feed toFeed(Feeds feeds){
		if(feeds == null) return null;
		
		Feed feed = new Feed();
		feed.setFeedId(feeds.getFeedsId());
		feed.setName( feeds.getName());
		feed.setDateFormat( feeds.getDateformat());
		feed.setUrl( feeds.getUrl());
		feed.setTimezone( feeds.getTimezone());
		feed.setHtmlFieldName( feeds.getHtmlFieldName());
		feed.setPublishedDateFieldName( feeds.getPublishedDateFieldName());
		feed.setLinkFieldName( feeds.getLinkFieldName());
		return feed;
	}
	
	public static Feeds toDbFeeds(Feed feed){
		if(feed == null) return null;
		
		Feeds feeds = new Feeds();
		feeds.setFeedsId( feed.getFeedId());
		return feeds;
	}

}
