package com.cee.news.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.net.ClassResourceWebClient;
import com.cee.news.parser.net.WebClient;

public class TestSiteParser {

	@Test
	public void testParse() throws IOException, SAXException {
		URL siteLocation = new URL("http://www.test.com/com/cee/news/parser/impl/spiegel.html");
		WebClient webClient = new ClassResourceWebClient();
		SiteParser parser = new SiteParser(new Parser(), new RomeFeedChecker(
				webClient), webClient);
		Site site = parser.parse(siteLocation);
		assertEquals("SPIEGEL ONLINE - Nachrichten", site.getTitle());
		assertTrue(site.getDescription().startsWith("Deutschlands f"));
		assertEquals(2, site.getFeeds().size());
		Feed feed = site.getFeeds().get(0);
		assertEquals("SPIEGEL ONLINE RSS Schlagzeilen", feed.getTitle());
		assertEquals("application/rss+xml", feed.getContentType());
		assertEquals(
				new URL(siteLocation, "spiegelSchlagzeilen.rss")
						.toExternalForm(),
				feed.getLocation());
		feed = site.getFeeds().get(1);
		assertEquals("SPIEGEL ONLINE RSS Nachrichten", feed.getTitle());
		assertEquals("application/rss+xml", feed.getContentType());
		assertEquals(
				new URL(siteLocation, "spiegelNachrichten.rss")
						.toExternalForm(),
				feed.getLocation());

	}

}
