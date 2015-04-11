package com.mcnavish.topposts.scraper;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mcnavish.topposts.domain.Post;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class RssScraperTest {

	private RssScraper rssScraper;
	
	@Before
	public void setup(){
		rssScraper = new RssScraper();
	}
	
	@Test
	public void getPostsTestSuccess() throws Exception{
		String expectedTitle1 = "some title 1";
		String expectedDescription1 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink1 = "http://feeds.gawker.com/~r/gizmodo/full/~3/LJEt6SjYyhc/htc-one-m9-teardown-dont-try-this-at-home-1695893624";
		String expectedPublishedDate1 = "Mon, 06 Apr 2015 04:00:00 GMT";
		
		String expectedTitle2 = "different title 2";
		String expectedDescription2 = "&lt;img src=\\\"http://i.kinja-img.com/gawker-media/image/upload/s--M8gRpWei--/c_fit,fl_progressive,q_80,w_636/tneazg5w8rhhuemudekt.jpg\\\" /&gt;";
		String expectedLink2 = "http://feeds.gawker.com/~r/gizmodo/full/~3/aZYB91vu0t8/the-best-easter-eggs-on-the-internet-1667711776";
		String expectedPublishedDate2 = "Mon, 06 Apr 2015 03:00:00 GMT";
		
		String responseWithItems = 
				"<rss xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:taxo=\"http://purl.org/rss/1.0/modules/taxonomy/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" version=\"2.0\">\n" + 
				"    <channel>\n" + 
				"        <title>Gizmodo</title>\n" + 
				"        <link>http://gizmodo.com</link>\n" + 
				"        <description>Everything Is Technology</description>\n" + 
				"        <language>en</language>\n" + 
				"        <pubDate>Mon, 06 Apr 2015 04:10:40 GMT</pubDate>\n" + 
				"        <lastBuildDate>Mon, 06 Apr 2015 04:10:40 GMT</lastBuildDate>\n" + 
				"        <ttl>30</ttl>\n" + 
				"        <feedburner:info uri=\"gizmodo/full\" />\n" + 
				"        <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"hub\" href=\"http://pubsubhubbub.appspot.com/\" />\n" + 
				"        <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"self\" type=\"application/rss+xml\" href=\"http://www.gizmodo.com/index.xml\" />\n" + 
				"        <feedburner:browserFriendly>This is an XML content feed. It is intended to be viewed in a newsreader or syndicated to another site.</feedburner:browserFriendly>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle1 + "</title>" + 
				"            <link>" + expectedLink1 + "</link>" + 
				"            <description>" + expectedDescription1 + "</description>" + 
				"			 <category domain=\"\">smartphones</category>\n" + 
				"            <category domain=\"\">htc one m9</category>\n" + 
				"            <category domain=\"\">ifixit</category>\n" + 
				"            <pubDate>" + expectedPublishedDate1 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1695893624</guid>\n" + 
				"            <dc:creator>Chris Mills</dc:creator>\n" + 
				"            <feedburner:origLink>http://gizmodo.com/htc-one-m9-teardown-dont-try-this-at-home-1695893624</feedburner:origLink>\n" + 
				"        </item>\n" + 
				"        <item>\n" + 
				"            <title>" + expectedTitle2 + "</title>\n" + 
				"            <link>" +  expectedLink2 + "</link>\n" + 
				"            <description>" + expectedDescription2 + "</description>" +
				" 			 <category domain=\"\">chatroom</category>\n" + 
				"            <category domain=\"\">easter eggs</category>\n" + 
				"            <category domain=\"\">internet</category>\n" + 
				"            <category domain=\"\">google</category>\n" + 
				"            <pubDate>" + expectedPublishedDate2 + "</pubDate>\n" + 
				"            <guid isPermaLink=\"false\">1667711776</guid>\n" + 
				"            <dc:creator>Chris Mills</dc:creator>\n" + 
				"            <feedburner:origLink>http://gizmodo.com/the-best-easter-eggs-on-the-internet-1667711776</feedburner:origLink>\n" + 
				"        </item>" +
				"   </channel>" +
				"</rss>";
		
		String feedUrl = "http://feeds.gawker.com/gizmodo/full";
		URL requestUrl = new URL(feedUrl);
		Document doc = Jsoup.parseBodyFragment(responseWithItems);
		String dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";
		
		PowerMockito.spy(Jsoup.class);
		PowerMockito.doReturn( doc ).when(Jsoup.class, "parse", requestUrl, 3000);
		DateTime minimumDate = DateTime.now().minusDays(1);
		List<Post> result =  rssScraper.extractPosts(feedUrl, minimumDate);
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expectedTitle1, result.get(0).getTitle());
		String unescapedHtml1 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals( unescapedHtml1, result.get(0).getHtml());
//		Assert.assertEquals(expectedLink1, result.get(0).getLink());
		Assert.assertEquals(verifyDate(expectedPublishedDate1, result.get(0).getPublishedDate(), dateFormat), true);
		
		
		Assert.assertEquals(expectedTitle2, result.get(1).getTitle());
		String unescapedHtml2 = StringEscapeUtils.unescapeHtml(expectedDescription1);
		Assert.assertEquals(unescapedHtml2, result.get(1).getHtml());
//		Assert.assertEquals(expectedLink2, result.get(1).getLink());
		Assert.assertEquals(verifyDate(expectedPublishedDate2, result.get(1).getPublishedDate(), dateFormat), true);
	}
	
	private boolean verifyDate(String expected, DateTime actual, String dateFormat){
		String expectedUtc = expected.replace("GMT", "UTC");
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(dateFormat);
		
		return expectedUtc.equals( actual.toString( dateFormatter ));
	}
}
