package com.mcnavish.topposts.domain;

import org.joda.time.DateTime;

public class Post{

	private String url;
	private String title;
	private String html;
	private DateTime publishedDate;
	private int faceBookLikes;
	private int twitterTweets;
	private Feed feed;

	
	public int getPostRating(){
		return faceBookLikes + twitterTweets;
	}
	
	public long getFeedId(){
		if(feed != null){
			return feed.getFeedId();
		}
		return -1;
	}
	
	public String toString(){
		return title;
	}
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getFaceBookLikes() {
		return faceBookLikes;
	}
	public void setFaceBookLikes(int faceBookLikes) {
		this.faceBookLikes = faceBookLikes;
	}
	public int getTwitterTweets() {
		return twitterTweets;
	}
	public void setTwitterTweets(int twitterTweets) {
		this.twitterTweets = twitterTweets;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public DateTime getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(DateTime publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Feed getFeed() {
		return feed;
	}
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
}
