package com.mcnavish.topposts.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	
	@RequestMapping("/react")
	public String index(Map<String, Object> model) throws Exception{	
		
//		model.put("data", data);
		return "index";
	}
	
}
