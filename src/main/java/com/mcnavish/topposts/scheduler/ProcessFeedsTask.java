package com.mcnavish.topposts.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.mcnavish.topposts.services.FeedService;

@Component
@ManagedResource
public class ProcessFeedsTask {
	
	@Autowired
	private FeedService feedService;

	private static Logger logger = LoggerFactory.getLogger(ProcessFeedsTask.class);
	
	@Scheduled(cron="0 0 3 * * *")
	public void processFeeds(){
		logger.debug("Start processFeeds");
		StopWatch stopWatch = new StopWatch("Process Feeds StopWatch");
		stopWatch.start("Start processFeeds");
		
		try{
			feedService.processFeeds();
		}
		catch(Exception ex){
			logger.error("Error processing feeds", ex);
		}
		
		
		stopWatch.stop();
		logger.debug("Complete processFeeds \n" + stopWatch.prettyPrint());
	}
	
	@ManagedOperation(description="Hook to manually force the processing of feeds.")
	public void forceProcessFeeds(){
		processFeeds();
	}

	public void setFeedService(FeedService feedService) {
		this.feedService = feedService;
	}
}
