package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;

public class RssScraper {
	
	private static Logger logger = LoggerFactory.getLogger(RssScraper.class);
	
	protected String YQL_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20feed%20where%20url=\"[[FEED_URL]]\"";

	public List<Post> getAllPosts(List<Feed> feeds, DateTime minimumDate){
		List<Post> posts = new ArrayList<Post>();
		for(Feed feed : feeds){
			posts.addAll(getPosts(feed, minimumDate));
		}
		
		return posts;
	}
	
	public List<Post> getPosts(Feed feed, DateTime minimumDate){
		List<Post> allPosts = new ArrayList<Post>();
		try{
			allPosts = extractPosts(feed, minimumDate);
		}
		catch(Exception ex){
			//TODO: mark feed status as 'error'
		}
		
		return allPosts;
	}
	
	protected String buildYqlUrl(String feedUrl){
		String newUrl = YQL_URL.replace("[[FEED_URL]]", feedUrl);
		return newUrl;
	}

	protected List<Post> extractPosts(Feed feed, DateTime minimumDate) throws IOException, SAXException, XPathExpressionException{
		List<Post> allPosts = new ArrayList<Post>();
		
		try{
			String url = buildYqlUrl( feed.getUrl());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc =  builder.parse(url);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			
			String itemExpression = "/query/results/item";
			XPathExpression expression = xpath.compile(itemExpression);
			NodeList items = (NodeList)expression.evaluate(doc, XPathConstants.NODESET);
			
			if(items == null || items.getLength() == 0){
				logger.error("no items found in feed for url: " + url);
				return allPosts;
			}
			DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(feed.getDateFormat());
			
			for(int i= 0; i < items.getLength(); i++){
				Node item = items.item(i);
				String title = xpath.evaluate("title", item);
				String pubDate = xpath.evaluate(feed.getPublishedDateFieldName(), item);
				logger.debug(title + " published Date: " + pubDate);				
				Post post = new Post();
				post.setTitle(title);
				post.setUrl(xpath.evaluate( feed.getLinkFieldName(), item));
				post.setHtml(xpath.evaluate(feed.getHtmlFieldName(), item) );
				DateTime dt = dateFormatter.parseDateTime(pubDate);
				
				if(dt.isBefore(minimumDate)) continue;
			
				post.setPublishedDate(dt);
				post.setFeed(feed);
				allPosts.add(post);
			}
			
		}
		catch(ParserConfigurationException ex){
			logger.error("Error setting up xPath");
		}
		catch(IOException | SAXException | XPathExpressionException ex){
			logger.error("Error getting posts", ex);
			throw ex;
		}
		catch(Exception ex){
			logger.error("Unhandled error!", ex);
			throw ex;
		}
		
		return allPosts;
	}
}
