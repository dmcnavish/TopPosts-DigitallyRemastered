package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class IHeartQuotesScraper {

	private static Logger logger = LoggerFactory.getLogger(RssScraper.class);
	private static String URL = "http://www.iheartquotes.com/api/v1/random?source=news&format=json";
	
	public static String getQuote() throws IOException, IllegalArgumentException{	
		String quote = "";
		try{
			URL endpoint = new URL(URL);
			InputStream input = endpoint.openStream();
			Reader reader = new InputStreamReader(input);
			IHeartQuotesResponse response = new Gson().fromJson(reader, IHeartQuotesResponse.class);
			if(response != null){
				quote = response.getQuote();
			}
		
		}catch(IOException ex){
			logger.error("Error getting twitter count for url" + URL, ex);
			throw ex;
		}
		catch(Exception ex){
			logger.error("Unhandled error!", ex);
			throw ex;
		}
		
		return quote;
	}
	
	protected class IHeartQuotesResponse{
		private String quote;
		private String json_class;
		private String[] tags;
		private String link;
		private String source;
		
		public String getQuote() {
			return quote;
		}
		public void setQuote(String quote) {
			this.quote = quote;
		}
		public String getJson_class() {
			return json_class;
		}
		public void setJson_class(String json_class) {
			this.json_class = json_class;
		}
		public String[] getTags() {
			return tags;
		}
		public void setTags(String[] tags) {
			this.tags = tags;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}

	}
}
