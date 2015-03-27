package org.cee.parser;

/*
 * #%L
 * Content Extraction Engine - News Parser
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
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

import org.cee.BaseWebClientTest;
import org.cee.SiteExtraction;
import org.cee.language.SiteLanguageDetector;
import org.cee.net.WebClient;
import org.cee.net.WebResponse;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.article.Article;
import org.cee.store.article.ArticleKey;
import org.cee.store.article.ArticleStore;
import org.cee.store.site.Feed;
import org.cee.store.site.Site;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class TestSiteReader extends BaseWebClientTest {

	@Test
	public void testReadFeed() throws IOException, ParserException {
		String location = "http://www.mysite.com/feed.rss";
		URL locationUrl = new URL(location);
		Feed feed = new Feed();
		Reader reader = createReader();
		WebClient webClient = createWebClient(reader);
		FeedParser feedParser = mock(FeedParser.class);
		when(feedParser.parse(reader, locationUrl)).thenReturn(feed);
		
		assertSame(feed, new SiteReader(null, null, feedParser, null, null).readFeed(webClient, location));
		verify(reader).close();
	}
	
	@Test(expected = IOException.class)
	public void testReadFeedThrowsIOException() throws MalformedURLException, ParserException, IOException {
		String location = "http://www.mysite.com/feed.rss";
		URL locationUrl = new URL(location);
		Reader reader = createReader();
        WebClient webClient = createWebClient(reader);
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
		when(webClient.openWebResponse(locationUrl, false)).thenReturn(response);
		Reader reader = mock(Reader.class);
		when(response.openReader()).thenReturn(reader);
		when(response.getLocation()).thenReturn(redirectedLocationUrl);//the request may have been redirected, site reader should use new location internally.
		
		SiteExtraction siteExtraction = new SiteExtraction();
		URL feed1Url = new URL("http://www.mysite.com/feed1.rss");
		URL feed2Url = new URL("http://www.mysite.com/feed2.rss");
		URL feed3Url = new URL("http://www.mysite.com/feed3.rss");
		siteExtraction.getFeedLocations().add(feed1Url);
		siteExtraction.getFeedLocations().add(feed2Url);
		siteExtraction.getFeedLocations().add(feed3Url);
		SiteParser siteParser = mock(SiteParser.class);
		when(siteParser.parse(reader, redirectedLocationUrl)).thenReturn(siteExtraction);
		
		Feed feed1 = new Feed();
		Feed feed2 = new Feed();
		Feed feed3 = new Feed();
		FeedParser feedParser = mock(FeedParser.class);
		Reader readerFeed1 = mock(Reader.class);
		Reader readerFeed2 = mock(Reader.class);
		Reader readerFeed3 = mock(Reader.class);
		addReaderUrls(webClient, new URL[]{feed1Url, feed2Url, feed3Url}, new Reader[]{readerFeed1, readerFeed2, readerFeed3});
	    when(feedParser.parse(readerFeed1, feed1Url)).thenReturn(feed1);
	    when(feedParser.parse(readerFeed2, feed2Url)).thenReturn(feed2);
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
		when(webClient.openWebResponse(locationUrl, false)).thenReturn(response);
		Reader reader = mock(Reader.class);
		when(response.openReader()).thenReturn(reader);
		when(response.getLocation()).thenReturn(locationUrl);
		
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
		addReaderUrls(webClient, new URL[]{feed1URL, feed3URL}, new Reader[]{feed1Reader, feed3Reader});
		
		Article existingArticle = new Article();
		existingArticle.setExternalId("existingArticle");
		Article unparsableArticle = new Article();
		unparsableArticle.setExternalId("unparseable");
		Article throwsException = new Article();
		throwsException.setExternalId("throwsException");
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
		feed3Articles.add(throwsException);
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
		when(articleReader.readArticle(webClient, throwsException)).thenThrow(new IOException());
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
	
	
	@Test
	public void testUpdateFeedParserThrowsIOException() throws IOException, ParserException, StoreException {
		Site site = new Site();
		site.setName("My Site");
		Feed feed1 = new Feed();
		feed1.setActive(true);
		feed1.setLocation("http://feed1");
		URL feed1URL = new URL(feed1.getLocation());
		Reader feed1Reader = mock(Reader.class);
		Feed feed2 = new Feed();
		feed2.setActive(true);
		feed2.setLocation("http://feed2");
		URL feed2URL = new URL(feed2.getLocation());
		Reader feed2Reader = mock(Reader.class);
		site.getFeeds().add(feed1);
		site.getFeeds().add(feed2);
		
		WebClient webClient = mock(WebClient.class);
		addReaderUrls(webClient, new URL[]{feed1URL, feed2URL}, new Reader[]{feed1Reader, feed2Reader});
		
		FeedParser feedParser = mock(FeedParser.class);
		
		when(feedParser.readArticles(same(feed1Reader), eq(feed1URL))).thenThrow(new IOException());
		when(feedParser.readArticles(same(feed2Reader), eq(feed2URL))).thenReturn(new ArrayList<Article>());
		
		new SiteReader(null, null, feedParser, null, null).update(webClient, site);
		verify(feedParser).readArticles(same(feed1Reader), eq(feed1URL));
		verify(feed1Reader).close();
		verify(feedParser).readArticles(same(feed2Reader), eq(feed2URL));
		verify(feed2Reader).close();	
	}
	
	@Test
	public void testUpdateWebClientThrowsIOExceptionForFeedUrl() throws IOException, ParserException, StoreException {
		Site site = new Site();
		site.setName("My Site");
		Feed feed1 = new Feed();
		feed1.setActive(true);
		feed1.setLocation("http://feed1");
		URL feed1URL = new URL(feed1.getLocation());
		Feed feed2 = new Feed();
		feed2.setActive(true);
		feed2.setLocation("http://feed2");
		URL feed2URL = new URL(feed2.getLocation());
		Reader feed2Reader = mock(Reader.class);
		site.getFeeds().add(feed1);
		site.getFeeds().add(feed2);
		
		WebClient webClient = mock(WebClient.class);
		addThrowsUrls(webClient, new URL[]{feed1URL});
		addReaderUrls(webClient, new URL[]{feed2URL}, new Reader[]{feed2Reader});
		
		FeedParser feedParser = mock(FeedParser.class);
		when(feedParser.readArticles(same(feed2Reader), eq(feed2URL))).thenReturn(new ArrayList<Article>());
		
		new SiteReader(null, null, feedParser, null, null).update(webClient, site);
		verify(feedParser).readArticles(same(feed2Reader), eq(feed2URL));
		verify(feed2Reader).close();	
	}
}
