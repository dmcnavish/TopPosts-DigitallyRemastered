package com.mcnavish.topposts.domain;

import java.util.ArrayList;
import java.util.List;

import com.mcnavish.topposts.hibernate.db.Feeds;
import com.mcnavish.topposts.hibernate.db.Posts;


public class DomainObjectConversion {
	
	public static List<Posts> toPosts(List<Post> allPosts){
		List<Posts> posts = new ArrayList<Posts>();
		
		for(Post post : allPosts){
			posts.add( toPosts(post));
		}
		
		return posts;
	}

	public static Posts toPosts(Post post){
		Posts posts = new Posts();
		posts.setFacebookLikes( post.getFaceBookLikes());
		if(post.getHtml() != null){
			posts.setHtml( post.getHtml().getBytes() );
		}
		posts.setUrl( post.getUrl());
		posts.setPublishedDate( post.getPublishedDate());
		posts.setTitle(post.getTitle());
		posts.setTwitterTweets( post.getTwitterTweets());
		posts.setFeeds( toFeeds( post.getFeed() ));
		return posts;
	}
	
	public static Feeds toFeeds(Feed feed){
		if(feed == null) return null;
		
		Feeds feeds = new Feeds();
		feeds.setFeedsId( feed.getFeedId());
		return feeds;
	}

}
