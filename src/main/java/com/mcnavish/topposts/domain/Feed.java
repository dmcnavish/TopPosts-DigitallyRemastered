package com.mcnavish.topposts.domain;

public class Feed {
	
	private long feedId;
	private String url;
	private String dateFormat;
	private String name;
	private String timezone;
	private String htmlFieldName;
	private String publishedDateFieldName;
	private String linkFieldName;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public long getFeedId() {
		return feedId;
	}
	public void setFeedId(long feedId) {
		this.feedId = feedId;
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
