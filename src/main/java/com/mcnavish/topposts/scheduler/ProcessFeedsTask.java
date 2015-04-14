package com.mcnavish.topposts.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.mcnavish.topposts.services.FeedService;

@Component
public class ProcessFeedsTask {
	
	@Autowired
	private FeedService feedService;

	private static Logger logger = LoggerFactory.getLogger(ProcessFeedsTask.class);
	
	@Scheduled(cron="0 0 23 * * *")
	public void processFeeds(){
		logger.debug("Start processFeeds");
		StopWatch stopWatch = new StopWatch("Process Feeds StopWatch");
		stopWatch.start("Start processFeeds");
		
		feedService.processFeeds();
		
		stopWatch.stop();
		logger.debug("Complete processFeeds \n" + stopWatch.prettyPrint());
	}
}
