package com.mcnavish.topposts.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mcnavish.topposts.services.FeedService;

@Controller
public class ViewController {

	
	@Autowired
	private FeedService feedService;
	
	@RequestMapping("/react")
	public String index(Map<String, Object> model) throws Exception{	
		
//		model.put("data", data);
		return "index";
	}
	
	@RequestMapping("/process")
	public String processFeeds(){
		
		feedService.processFeeds();
		
		return "success";
	}
	
}
