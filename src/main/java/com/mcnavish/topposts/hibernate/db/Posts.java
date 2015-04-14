package com.mcnavish.topposts.hibernate.db;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

@Entity
@Table( name = "Posts")
public class Posts implements CommonHibernate{

	private long postsId;
	private Feeds feeds;
	private String url;
	private String title;
	private byte[] html;
	private DateTime publishedDate;
	private int facebookLikes;
	private int twitterTweets;
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public long getPostsId() {
		return postsId;
	}
	public void setPostsId(long postsId) {
		this.postsId = postsId;
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
	public int getTwitterTweets() {
		return twitterTweets;
	}
	public void setTwitterTweets(int twitterTweets) {
		this.twitterTweets = twitterTweets;
	}
	public byte[] getHtml() {
		return html;
	}
	public void setHtml(byte[] html) {
		this.html = html;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = Feeds.class)
	@JoinColumn(name="feedsId")
	public Feeds getFeeds() {
		return feeds;
	}
	public void setFeeds(Feeds feeds) {
		this.feeds = feeds;
	}
	public int getFacebookLikes() {
		return facebookLikes;
	}
	public void setFacebookLikes(int facebookLikes) {
		this.facebookLikes = facebookLikes;
	}
}
