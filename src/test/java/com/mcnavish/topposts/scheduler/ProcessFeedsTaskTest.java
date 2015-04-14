package com.mcnavish.topposts.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mcnavish.topposts.services.FeedService;

@RunWith(PowerMockRunner.class)
//@PrepareForTest(FeedService.class)
public class ProcessFeedsTaskTest {

	
	@Mock
	private FeedService feedService;
	
	private ProcessFeedsTask processFeedsTask;
	
	@Before
	public void setup(){
		processFeedsTask = new ProcessFeedsTask();
		processFeedsTask.setFeedService(feedService);
	}
	
	@Test
	public void processFeedsTestSuccess() throws Exception{
		
		//mocks
		PowerMockito.doNothing().when(feedService).processFeeds();
		
		processFeedsTask.processFeeds();
		
		Mockito.verify(feedService, Mockito.times(1)).processFeeds();
	}
	
	@Test
	public void processFeedsTestFail() throws Exception{
		
		//mocks
		PowerMockito.doThrow(new Exception()).when(feedService).processFeeds();
		
		processFeedsTask.processFeeds();
		
		Assert.assertTrue(true);
		
	}
}
