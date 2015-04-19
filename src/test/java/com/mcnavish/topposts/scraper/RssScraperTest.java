package com.mcnavish.topposts.scraper;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.InputSource;

import com.mcnavish.topposts.domain.Feed;
import com.mcnavish.topposts.domain.Post;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DocumentBuilder.class, DocumentBuilderFactory.class})
public class RssScraperTest {

	private RssScraper rssScraper;
	
	@Before
	public void setup(){
		rssScraper = new RssScraper();
	}
	
	@Test
	public void getPostsTest_feedBurner_Success() throws Exception{
		String dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
		DateTime minimumDate = DateTime.now().minusDays(1);
		minimumDate.toString(dateFormatter);
		
		String expectedTitle1 = "some title 1";
		String expectedDescription1 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink1 = "http://feeds.gawker.com/~r/gizmodo/full/~3/LJEt6SjYyhc/htc-one-m9-teardown-dont-try-this-at-home-1695893624";
		String expectedPublishedDate1 = minimumDate.plusHours(1).toString(dateFormatter);
		
		String expectedTitle2 = "different title 2";
		String expectedDescription2 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink2 = "http://feeds.gawker.com/~r/gizmodo/full/~3/aZYB91vu0t8/the-best-easter-eggs-on-the-internet-1667711776";
		String expectedPublishedDate2 = minimumDate.plusHours(2).toString(dateFormatter);
		
		String responseWithItems = 				
				"<query yahoo:count=\"20\" yahoo:created=\"2015-04-12T16:07:03Z\" yahoo:lang=\"en-us\" xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\">\n" + 
				"    <results>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle1 + "</title>\n" + 
				"            <link>" + expectedLink1 + "</link>\n" + 
				"            <description>" + expectedDescription1 + "</description>" +
				"			 <category domain=\"\">in the making</category>\n" + 
				"            <category domain=\"\">thermal batteries</category>\n" + 
				"            <category domain=\"\">useful science</category>\n" + 
				"            <pubDate>" + expectedPublishedDate1 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697312107</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">http://someotherurl</feedburner:origLink>\n" + 
				"        </item>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle2 + "</title>\n" + 
				"            <link>" + expectedLink2 + "</link>\n" + 
				"            <description>" + expectedDescription2 + "</description>" +
				"			 <category domain=\"\">nasa</category>\n" + 
				"            <category domain=\"\">astronomy</category>\n" + 
				"            <category domain=\"\">hubble 25th anniversary</category>\n" + 
				"            <category domain=\"\">hubble</category>\n" + 
				"            <category domain=\"\">space</category>\n" + 
				"            <pubDate>" + expectedPublishedDate2 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697233864</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">https://wrongurl</feedburner:origLink>\n" + 
				"        </item>" +
				"	</results>" +
				"</query>";
		
		
		
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		
		mockXpath(responseWithItems, url);
		
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		feed.setDateFormat(dateFormat);
		feed.setHtmlFieldName("description");
		feed.setLinkFieldName("link");
		feed.setPublishedDateFieldName("pubDate");
		feed.setTimezone("UTC");

		List<Post> result =  rssScraper.extractPosts(feed, minimumDate);
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expectedTitle1, result.get(0).getTitle());
		String unescapedHtml1 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals( unescapedHtml1, result.get(0).getHtml());
		Assert.assertEquals(expectedLink1, result.get(0).getUrl());
		Assert.assertEquals(verifyDate(expectedPublishedDate1, result.get(0).getPublishedDate(), dateFormat), true);
		
		
		Assert.assertEquals(expectedTitle2, result.get(1).getTitle());
		String unescapedHtml2 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals(unescapedHtml2, result.get(1).getHtml());
		Assert.assertEquals(expectedLink2, result.get(1).getUrl());
		Assert.assertEquals(verifyDate(expectedPublishedDate2, result.get(1).getPublishedDate(), dateFormat), true);
	}
	
	@Test
	public void getPostsTest_feedBurner_nodeWithColon() throws Exception{
		String dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
		DateTime minimumDate = DateTime.now().minusDays(1);
		minimumDate.toString(dateFormatter);
		
		String expectedTitle1 = "some title 1";
		String expectedDescription1 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink1 = "http://feeds.gawker.com/~r/gizmodo/full/~3/LJEt6SjYyhc/htc-one-m9-teardown-dont-try-this-at-home-1695893624";
		String expectedPublishedDate1 = minimumDate.plusHours(1).toString(dateFormatter);
		
		String responseWithItems = 				
				"<query yahoo:count=\"20\" yahoo:created=\"2015-04-12T16:07:03Z\" yahoo:lang=\"en-us\" xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\">\n" + 
				"    <results>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle1 + "</title>\n" + 
				"            <link>" + expectedLink1 + "</link>\n" + 
				"            <content:encoded xmlns:content=\"http://purl.org/rss/1.0/modules/content/\">" + expectedDescription1 + "</content:encoded>" +
				"			 <category domain=\"\">in the making</category>\n" + 
				"            <category domain=\"\">thermal batteries</category>\n" + 
				"            <category domain=\"\">useful science</category>\n" + 
				"            <pubDate>" + expectedPublishedDate1 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697312107</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">http://someotherurl</feedburner:origLink>\n" + 
				"        </item>\n" + 
				"	</results>" +
				"</query>";
		
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		
		mockXpath(responseWithItems, url);
		
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		feed.setDateFormat(dateFormat);
		feed.setHtmlFieldName("//*[name()='content:encoded']");
		feed.setLinkFieldName("link");
		feed.setPublishedDateFieldName("pubDate");
		feed.setTimezone("UTC");

		List<Post> result =  rssScraper.extractPosts(feed, minimumDate);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expectedTitle1, result.get(0).getTitle());
		String unescapedHtml1 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals( unescapedHtml1, result.get(0).getHtml());
		Assert.assertEquals(expectedLink1, result.get(0).getUrl());
		Assert.assertEquals(verifyDate(expectedPublishedDate1, result.get(0).getPublishedDate(), dateFormat), true);
	}
	
	@Test
	public void getPostsTest_feedBurner_noResults() throws Exception{
		String responseWithoutItems = 
				"<query yahoo:count=\"20\" yahoo:created=\"2015-04-12T16:07:03Z\" yahoo:lang=\"en-us\" xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\">" + 
				"    <results>" + 
				"	 </results>" +
				"</query>";
		
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		
		mockXpath(responseWithoutItems, url);
		
		DateTime minimumDate = DateTime.now().minusDays(1);
		List<Post> result =  rssScraper.extractPosts(feed, minimumDate);
		
		Assert.assertEquals(0, result.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getPostsTest_feedBurner_invalidDateFormat() throws Exception{
		String expectedTitle1 = "some title 1";
		String expectedDescription1 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink1 = "http://feeds.gawker.com/~r/gizmodo/full/~3/LJEt6SjYyhc/htc-one-m9-teardown-dont-try-this-at-home-1695893624";
		String expectedPublishedDate1 = "Mon, 06 Apr 2015 04:00:00 -0500";
		
		String responseWithItems = 
				"<query yahoo:count=\"20\" yahoo:created=\"2015-04-12T16:07:03Z\" yahoo:lang=\"en-us\" xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\">\n" + 
				"    <results>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle1 + "</title>\n" + 
				"            <link>" + expectedLink1 + "</link>\n" + 
				"            <description>" + expectedDescription1 + "</description>" +
				"			 <category domain=\"\">in the making</category>\n" + 
				"            <category domain=\"\">thermal batteries</category>\n" + 
				"            <category domain=\"\">useful science</category>\n" + 
				"            <pubDate>" + expectedPublishedDate1 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697312107</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">wronglink</feedburner:origLink>\n" + 
				"        </item>\n" + 
				"	</results>" +
				"</query>";
	
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		String incorrectDateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
		
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		feed.setDateFormat(incorrectDateFormat);
		feed.setHtmlFieldName("description");
		feed.setLinkFieldName("link");
		feed.setPublishedDateFieldName("pubDate");
		feed.setTimezone("UTC");
		
		mockXpath(responseWithItems, url);
		
		DateTime minimumDate = DateTime.now().minusDays(1);
		rssScraper.extractPosts(feed, minimumDate);
	}
	
	@Test
	public void getPosts_someOld() throws Exception{
		String dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
		DateTime minimumDate = DateTime.now().minusDays(1);
		minimumDate.toString(dateFormatter);
		
		String expectedTitle1 = "some title 1";
		String expectedDescription1 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink1 = "http://feeds.gawker.com/~r/gizmodo/full/~3/LJEt6SjYyhc/htc-one-m9-teardown-dont-try-this-at-home-1695893624";
		String expectedPublishedDate1 = minimumDate.minusHours(1).toString(dateFormatter);
		
		String expectedTitle2 = "different title 2";
		String expectedDescription2 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink2 = "http://feeds.gawker.com/~r/gizmodo/full/~3/aZYB91vu0t8/the-best-easter-eggs-on-the-internet-1667711776";
		String expectedPublishedDate2 = minimumDate.plusHours(2).toString(dateFormatter);
		
		String responseWithItems = 				
				"<query yahoo:count=\"20\" yahoo:created=\"2015-04-12T16:07:03Z\" yahoo:lang=\"en-us\" xmlns:yahoo=\"http://www.yahooapis.com/v1/base.rng\">\n" + 
				"    <results>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle1 + "</title>\n" + 
				"            <link>" + expectedLink1 + "</link>\n" + 
				"            <description>" + expectedDescription1 + "</description>" +
				"			 <category domain=\"\">in the making</category>\n" + 
				"            <category domain=\"\">thermal batteries</category>\n" + 
				"            <category domain=\"\">useful science</category>\n" + 
				"            <pubDate>" + expectedPublishedDate1 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697312107</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">wrong link</feedburner:origLink>\n" + 
				"        </item>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle2 + "</title>\n" + 
				"            <link>" + expectedLink2 + "</link>\n" + 
				"            <description>" + expectedDescription2 + "</description>" +
				"			 <category domain=\"\">nasa</category>\n" + 
				"            <category domain=\"\">astronomy</category>\n" + 
				"            <category domain=\"\">hubble 25th anniversary</category>\n" + 
				"            <category domain=\"\">hubble</category>\n" + 
				"            <category domain=\"\">space</category>\n" + 
				"            <pubDate>" + expectedPublishedDate2 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1697233864</guid>\n" + 
				"            <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Maddie Stone</dc:creator>\n" + 
				"            <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">wronglink</feedburner:origLink>\n" + 
				"        </item>" +
				"	</results>" +
				"</query>";
		
		
		
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		feed.setDateFormat(dateFormat);
		feed.setHtmlFieldName("description");
		feed.setLinkFieldName("link");
		feed.setPublishedDateFieldName("pubDate");
		feed.setTimezone("UTC");

		mockXpath(responseWithItems, url);
		
		List<Post> result =  rssScraper.extractPosts(feed, minimumDate);
		
		Assert.assertEquals(1, result.size());		
		Assert.assertEquals(expectedTitle2, result.get(0).getTitle());
		String unescapedHtml2 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals(unescapedHtml2, result.get(0).getHtml());
		Assert.assertEquals(expectedLink2, result.get(0).getUrl());
		Assert.assertEquals(verifyDate(expectedPublishedDate2, result.get(0).getPublishedDate(), dateFormat), true);
	}
	
	@Test(expected=IOException.class)
	public void getPostsTest_feedBurner_SocketTimeout() throws Exception{
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		String url = rssScraper.buildYqlUrl(feedUrl);
		
		Feed feed = new Feed();
		feed.setUrl(feedUrl);
		
		//mocks
		DocumentBuilderFactory builderFactory = PowerMockito.mock(DocumentBuilderFactory.class);
		DocumentBuilder builder = PowerMockito.mock(DocumentBuilder.class);
		
		PowerMockito.spy(DocumentBuilderFactory.class);
		PowerMockito.doReturn(builderFactory).when(DocumentBuilderFactory.class, "newInstance");
		PowerMockito.when(builderFactory.newDocumentBuilder()).thenReturn(builder);
		PowerMockito.when(builder.parse(url)).thenThrow( new IOException("Forced IOException!!") );
		
		DateTime minimumDate = DateTime.now().minusDays(1);
		rssScraper.extractPosts(feed, minimumDate);
	}
	
	private void mockXpath(String source, String url) throws Exception{
		DocumentBuilderFactory testBuilderfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder testBuilder = testBuilderfactory.newDocumentBuilder();
		InputSource inputSource = new InputSource(new StringReader(source));
		org.w3c.dom.Document doc =  testBuilder.parse(inputSource);
		
		//mocks
		DocumentBuilderFactory builderFactory = PowerMockito.mock(DocumentBuilderFactory.class);
		DocumentBuilder builder = PowerMockito.mock(DocumentBuilder.class);
		
		PowerMockito.spy(DocumentBuilderFactory.class);
		PowerMockito.doReturn(builderFactory).when(DocumentBuilderFactory.class, "newInstance");
		PowerMockito.when(builderFactory.newDocumentBuilder()).thenReturn(builder);
		PowerMockito.when(builder.parse(url)).thenReturn(doc);
	}
	
	
	private boolean verifyDate(String expected, DateTime actual, String dateFormat){
		String expectedUtc = expected.replace("GMT", "UTC");
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
		
		return expectedUtc.equals( actual.toString( dateFormatter ));
	}
	
}
