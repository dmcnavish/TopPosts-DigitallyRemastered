package com.mcnavish.topposts.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PostRatingDecreasingComparatorTest {

	private PostRatingDecreasingComparator comparator;
	
	@Before
	public void setup(){
		comparator = new PostRatingDecreasingComparator();
	}
	
	@Test
	public void testCompare_aLarger(){
		
		Post a = new Post();
		a.setFaceBookLikes(10);
		a.setTwitterTweets(10);
		
		Post b = new Post();
		b.setFaceBookLikes(2);
		b.setTwitterTweets(2);
		
		int result = comparator.compare(a, b);
		Assert.assertEquals(-1, result);
	}
	
	@Test
	public void testCompare_bLarger(){
		
		Post a = new Post();
		a.setFaceBookLikes(1);
		a.setTwitterTweets(1);
		
		Post b = new Post();
		b.setFaceBookLikes(42);
		b.setTwitterTweets(42);
		
		int result = comparator.compare(a, b);
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void testCompare_equal(){
		
		Post a = new Post();
		a.setFaceBookLikes(42);
		a.setTwitterTweets(42);
		
		Post b = new Post();
		b.setFaceBookLikes(42);
		b.setTwitterTweets(42);
		
		int result = comparator.compare(a, b);
		Assert.assertEquals(0, result);
	}
}
