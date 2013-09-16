package com.cee.news.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;
import com.cee.news.language.SiteLanguageDetector;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.SiteReader;
import com.cee.news.parser.ArticleReader;
import com.cee.news.parser.net.ReaderSource;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;

public class TestSiteReader {

	@Test
	public void testReadFeed() throws IOException, ParserException {
		String location = "http://www.mysite.com/feed.rss";
		URL locationUrl = new URL(location);
		Feed feed = new Feed();
		Reader reader = mock(Reader.class);
		WebClient webClient = mock(WebClient.class);
		when(webClient.openReader(locationUrl)).thenReturn(reader);
		FeedParser feedParser = mock(FeedParser.class);
		when(feedParser.parse(reader, locationUrl)).thenReturn(feed);
		
		assertSame(feed, new SiteReader(null, null, feedParser, null, null).readFeed(webClient, location));
		verify(reader).close();
	}
	
	@Test
	public void testReadSite() throws IOException, ParserException {
		String location = "http://www.mysite.com";
		String redirectedLocation = "http://www.mysite.com/redirect";
		URL locationUrl = new URL(location);
		URL redirectedLocationUrl = new URL(redirectedLocation);
		WebClient webClient = mock(WebClient.class);
		WebResponse response = mock(WebResponse.class);
		when(webClient.openWebResponse(locationUrl)).thenReturn(response);
		ReaderSource readerSource = mock(ReaderSource.class);
		when(response.openReaderSource()).thenReturn(readerSource);
		when(response.getLocation()).thenReturn(redirectedLocationUrl);//the request may have been redirected, site reader should use new location internally.
		Reader reader = mock(Reader.class);
		when(readerSource.getReader()).thenReturn(reader);
		
		SiteExtraction siteExtraction = new SiteExtraction();
		URL feed1Url = new URL("http://www.mysite.com/feed1.rss");
		URL feed2Url = new URL("http://www.mysite.com/feed2.rss");
		URL feed3Url = new URL("http://www.mysite.com/feed3.rss");
		siteExtraction.getFeedLocations().add(feed1Url);
		siteExtraction.getFeedLocations().add(feed2Url);
		siteExtraction.getFeedLocations().add(feed3Url);
		SiteParser siteParser = mock(SiteParser.class);
		when(siteParser.parse(reader, locationUrl)).thenReturn(siteExtraction);
		
		Feed feed1 = new Feed();
		Feed feed2 = new Feed();
		Feed feed3 = new Feed();
		FeedParser feedParser = mock(FeedParser.class);
		Reader readerFeed1 = mock(Reader.class);
		Reader readerFeed2 = mock(Reader.class);
		Reader readerFeed3 = mock(Reader.class);
		when(webClient.openReader(feed1Url)).thenReturn(readerFeed1);
		when(feedParser.parse(readerFeed1, feed1Url)).thenReturn(feed1);
		when(webClient.openReader(feed2Url)).thenReturn(readerFeed2);
		when(feedParser.parse(readerFeed2, feed2Url)).thenReturn(feed2);
		when(webClient.openReader(feed3Url)).thenReturn(readerFeed3);
		when(feedParser.parse(readerFeed3, feed3Url)).thenReturn(feed3);
		
		SiteLanguageDetector siteLanguageDetector = mock(SiteLanguageDetector.class);
		when(siteLanguageDetector.detect(siteExtraction)).thenReturn("en");
		
		Site site = new SiteReader(null, null, feedParser, siteParser, siteLanguageDetector).readSite(webClient, location);
		assertEquals("en", site.getLanguage());
		assertEquals(redirectedLocation, site.getLocation());
		assertEquals(3, site.getFeeds().size());
		assertSame(feed1, site.getFeeds().get(0));
		assertSame(feed2, site.getFeeds().get(1));
		assertSame(feed3, site.getFeeds().get(2));
		assertEquals("en", site.getLanguage());
		
		verify(readerFeed1).close();
		verify(readerFeed2).close();
		verify(readerFeed3).close();
		verify(reader).close();
	}
	
	/*
	private SiteReader createSiteReader() {
		FeedParser feedParser = new RomeFeedParser(new TagsoupXmlReaderFactory());
		SiteParser siteParser = new SiteParserImpl(new TagsoupXmlReaderFactory());
		ArticleParser articleParser = new BoilerpipeArticleParser(new TagsoupXmlReaderFactory());
		LanguageDetector languageDetector = new LanguageDetector() {
			@Override
			public String detect(String text) {
				return "ko";
			}
		};
		List<LanguageDetector> detectors = new ArrayList<LanguageDetector>();
		detectors.add(languageDetector);
		SiteReader siteReader = new SiteReader(null, new ArticleReader(articleParser), feedParser, siteParser, new SiteLanguageDetector(detectors));
		return siteReader;
	}import com.cee.news.parser.net.impl.ClassResourceWebClient;

	
    @Test
    public void testReadSite() throws IOException, ParserException {
        URL siteLocation = new URL("http://www.test.com/com/cee/news/parser/impl/spiegel.html");
        WebClient webClient = new ClassResourceWebClient();
        SiteReader siteReader = createSiteReader();
        
        Site site = siteReader.readSite(webClient, siteLocation.toExternalForm());
        assertEquals("SPIEGEL ONLINE - Nachrichten", site.getTitle());
        assertTrue(site.getDescription().startsWith("Deutschlands f"));
        assertEquals("de", site.getLanguage());
        assertEquals(2, site.getFeeds().size());
        Feed feed = site.getFeeds().get(0);
        assertEquals("SPIEGEL ONLINE - Schlagzeilen", feed.getTitle());
        assertEquals("de", feed.getLanguage());
        assertEquals(new URL(siteLocation, "spiegelSchlagzeilen.rss").toExternalForm(), feed.getLocation());
        feed = site.getFeeds().get(1);
        assertEquals("SPIEGEL ONLINE - Nachrichten", feed.getTitle());
        assertEquals("de", feed.getLanguage());
        assertEquals(new URL(siteLocation, "spiegelNachrichten.rss").toExternalForm(), feed.getLocation());

    }
 
	@Test
    public void testParseRegressionIssue190() throws ParserException, IOException {
        URL siteLocation = new URL("http://www.faz.de");
        
        // create mock for web client which simulates HTTP redirect from faz.de to faz.net
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Reader reader = new InputStreamReader(inputStream);
        WebResponse response = Mockito.mock(WebResponse.class);
        Mockito.when(response.getLocation()).thenReturn(new URL("http://www.faz.net"));
        Mockito.when(response.openStream()).thenReturn(inputStream);
        Mockito.when(response.openReaderSource()).thenReturn(new ReaderSource(reader, null));
        WebClient webClient = Mockito.mock(WebClient.class);
        Mockito.when(webClient.openWebResponse(siteLocation)).thenReturn(response);
        
        SiteReader siteReader = createSiteReader();
        try {
        	Site site = siteReader.readSite(webClient, "http://www.faz.de");
            assertEquals(site.getLocation(), "http://www.faz.net");
        } finally {
        	IOUtils.closeQuietly(reader);
        }
    }
    */
}
