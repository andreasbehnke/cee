package com.cee.news.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.cee.news.SiteExtraction;
import com.cee.news.language.SiteLanguageDetector;
import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.net.ReaderSource;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

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
	
	@Test(expected = IOException.class)
	public void testReadFeedThrowsIOException() throws MalformedURLException, ParserException, IOException {
		String location = "http://www.mysite.com/feed.rss";
		URL locationUrl = new URL(location);
		Reader reader = mock(Reader.class);
		WebClient webClient = mock(WebClient.class);
		when(webClient.openReader(locationUrl)).thenReturn(reader);
		FeedParser feedParser = mock(FeedParser.class);
		when(feedParser.parse(reader, locationUrl)).thenThrow(new IOException());
		try {
			new SiteReader(null, null, feedParser, null, null).readFeed(webClient, location);	
		} finally {
			verify(reader).close();	
		}
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
	
	@Test(expected = IOException.class)
	public void testReadSiteThrowsIOException() throws IOException, ParserException {
		String location = "http://www.mysite.com";
		URL locationUrl = new URL(location);
		WebClient webClient = mock(WebClient.class);
		WebResponse response = mock(WebResponse.class);
		when(webClient.openWebResponse(locationUrl)).thenReturn(response);
		ReaderSource readerSource = mock(ReaderSource.class);
		when(response.openReaderSource()).thenReturn(readerSource);
		Reader reader = mock(Reader.class);
		when(readerSource.getReader()).thenReturn(reader);
		
		SiteParser siteParser = mock(SiteParser.class);
		when(siteParser.parse(reader, locationUrl)).thenThrow(new IOException());
		
		try {
			new SiteReader(null, null, null, siteParser, null).readSite(webClient, location);
		} finally {
			verify(reader).close();
		}
	}
	
	private class IsArticleListOfNElements extends ArgumentMatcher<List<Article>> {
		
		private final int n;
		
		public IsArticleListOfNElements(int n) {
			this.n = n;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean matches(Object list) {
			return ((List<Article>)list).size() == n;
		}
		
	}
	
	@Test
	public void testUpdate() throws IOException, ParserException, StoreException {
		Site site = new Site();
		site.setName("My Site");
		site.setLanguage("en");
		Feed feed1 = new Feed();
		feed1.setActive(true);
		feed1.setLocation("http://feed1");
		URL feed1URL = new URL(feed1.getLocation());
		Reader feed1Reader = mock(Reader.class);
		Feed inactiveFeed = new Feed();
		inactiveFeed.setActive(false);
		inactiveFeed.setLocation("http://feed2");
		Feed feedWithoutArticles = new Feed();
		feedWithoutArticles.setActive(true);
		feedWithoutArticles.setLocation("http://feed3");
		URL feed3URL = new URL(feedWithoutArticles.getLocation());
		Reader feed3Reader = mock(Reader.class);
		site.getFeeds().add(feed1);
		site.getFeeds().add(inactiveFeed);
		site.getFeeds().add(feedWithoutArticles);
		
		WebClient webClient = mock(WebClient.class, RETURNS_SMART_NULLS);
		when(webClient.openReader(eq(feed1URL))).thenReturn(feed1Reader);
		when(webClient.openReader(eq(feed3URL))).thenReturn(feed3Reader);
		
		Article existingArticle = new Article();
		existingArticle.setExternalId("existingArticle");
		Article unparsableArticle = new Article();
		unparsableArticle.setExternalId("unparseable");
		Article article1 = new Article();
		article1.setExternalId("article1");
		Article article2 = new Article();
		article2.setExternalId("article2");
		
		FeedParser feedParser = mock(FeedParser.class, RETURNS_SMART_NULLS);
		List<Article> feed1Articles = new ArrayList<Article>();
		when(feedParser.readArticles(feed1Reader, feed1URL)).thenReturn(feed1Articles);
		List<Article> feed3Articles = new ArrayList<Article>();
		feed3Articles.add(unparsableArticle);
		feed3Articles.add(existingArticle);
		feed3Articles.add(article1);
		feed3Articles.add(article2);
		when(feedParser.readArticles(feed3Reader, feed3URL)).thenReturn(feed3Articles);
		
		ArticleStore articleStore = mock(ArticleStore.class, RETURNS_SMART_NULLS);
		when(articleStore.contains(eq(EntityKey.get("My Site")), eq("existingArticle"))).thenReturn(true);
		when(articleStore.contains(eq(EntityKey.get("My Site")), eq("unparseable"))).thenReturn(false);
		when(articleStore.contains(eq(EntityKey.get("My Site")), eq("article1"))).thenReturn(false);
		when(articleStore.contains(eq(EntityKey.get("My Site")), eq("article2"))).thenReturn(false);

		ArticleReader articleReader = mock(ArticleReader.class, RETURNS_SMART_NULLS);
		when(articleReader.readArticle(webClient, unparsableArticle)).thenReturn(null);
		when(articleReader.readArticle(webClient, article1)).thenReturn(article1);
		when(articleReader.readArticle(webClient, article2)).thenReturn(article2);
		
		@SuppressWarnings("unchecked")
		List<ArticleKey> feed1ArticleKeys = mock(List.class, RETURNS_SMART_NULLS);
		when(feed1ArticleKeys.size()).thenReturn(2);
		@SuppressWarnings("unchecked")
		List<ArticleKey> feed3ArticleKeys = mock(List.class, RETURNS_SMART_NULLS);
		when(feed3ArticleKeys.size()).thenReturn(0);
		when(articleStore.addNewArticles(eq(EntityKey.get("My Site")), argThat(new IsArticleListOfNElements(2)))).thenReturn(feed1ArticleKeys);
		when(articleStore.addNewArticles(eq(EntityKey.get("My Site")), argThat(new IsArticleListOfNElements(0)))).thenReturn(feed3ArticleKeys);

		assertEquals(2, new SiteReader(articleStore, articleReader, feedParser, null, null).update(webClient, site));
		
		verify(feed1Reader).close();
		verify(feed3Reader).close();
	}
	
	@Test(expected = IOException.class)
	public void testUpdateThrowsIOException() throws IOException, ParserException, StoreException {
		Site site = new Site();
		site.setName("My Site");
		Feed feed1 = new Feed();
		feed1.setActive(true);
		feed1.setLocation("http://feed1");
		URL feed1URL = new URL(feed1.getLocation());
		Reader feed1Reader = mock(Reader.class);
		site.getFeeds().add(feed1);
		
		WebClient webClient = mock(WebClient.class, RETURNS_SMART_NULLS);
		when(webClient.openReader(eq(feed1URL))).thenReturn(feed1Reader);
		
		FeedParser feedParser = mock(FeedParser.class, RETURNS_SMART_NULLS);
		when(feedParser.readArticles(feed1Reader, feed1URL)).thenThrow(new IOException());

		try {
			new SiteReader(null, null, feedParser, null, null).update(webClient, site);
		} finally {
			verify(feed1Reader).close();	
		}	
	}
}
