package com.mcnavish.topposts.hibernate.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedTypes")
public enum FeedTypes {
	
	UNKNOWN (0),
	FEED_BURNER (1);
	
	@Id
	private int feedTypesId;
	
	private FeedTypes(int feedTypesId){
		this.feedTypesId = feedTypesId;
	}

	public int getFeedTypesId() {
		return feedTypesId;
	}

	public void setFeedTypesId(int feedTypesId) {
		this.feedTypesId = feedTypesId;
	}
}
