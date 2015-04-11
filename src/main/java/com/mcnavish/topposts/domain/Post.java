package com.mcnavish.topposts.domain;

import org.joda.time.DateTime;

public class Post {

	private String link;
	private String title;
	private String html;
	private DateTime publishedDate;
	private int faceBookLikes;
	private int twitterTweets;
	
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public DateTime getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(DateTime publishedDate) {
		this.publishedDate = publishedDate;
	}
}
