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
	private String name;
	private FeedTypes feedTypes;
	private String url;
	private String dateformat;
	private String timezone;
	private String htmlFieldName;
	private String publishedDateFieldName;
	private String linkFieldName;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getHtmlFieldName() {
		return htmlFieldName;
	}
	public void setHtmlFieldName(String htmlFieldName) {
		this.htmlFieldName = htmlFieldName;
	}
	public String getPublishedDateFieldName() {
		return publishedDateFieldName;
	}
	public void setPublishedDateFieldName(String publishedDateFieldName) {
		this.publishedDateFieldName = publishedDateFieldName;
	}
	public String getLinkFieldName() {
		return linkFieldName;
	}
	public void setLinkFieldName(String linkFieldName) {
		this.linkFieldName = linkFieldName;
	}
}
