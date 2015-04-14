package com.mcnavish.topposts.hibernate.db;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="feeds")
public class Feeds implements CommonHibernate{

	private long feedsId;
	private FeedTypes feedTypes;
	private String url;
	private String dateformat;
	private List<Posts> posts;
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public long getFeedsId() {
		return feedsId;
	}
	public void setFeedsId(long feedsId) {
		this.feedsId = feedsId;
	}
	
	@Enumerated(EnumType.ORDINAL)
	public FeedTypes getFeedTypes() {
		return feedTypes;
	}
	public void setFeedTypes(FeedTypes feedTypes) {
		this.feedTypes = feedTypes;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	
	@OneToMany(fetch = FetchType.LAZY)
	public List<Posts> getPosts() {
		return posts;
	}
	public void setPosts(List<Posts> posts) {
		this.posts = posts;
	}
}
