package com.mcnavish.topposts.domain;

import java.util.Comparator;

public class PostRatingDecreasingComparator implements Comparator<Post>{
	public int compare(Post a, Post b){
		if(a.getPostRating() > b.getPostRating()) return -1;
		else if(a.getPostRating() < b.getPostRating()) return 1;
		return 0;
	}
}
