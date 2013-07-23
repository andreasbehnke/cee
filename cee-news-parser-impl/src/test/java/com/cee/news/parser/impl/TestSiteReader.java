package com.cee.news.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;
import org.mockito.Mockito;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.parser.net.impl.ClassResourceWebClient;

public class TestSiteReader {
	
	private SiteReader createSiteReader() {
		FeedParser feedParser = new RomeFeedParser(new Parser());
		SiteParser siteParser = new SiteParserImpl(new Parser());
		ArticleParser articleParser = new BoilerpipeArticleParser(new Parser());
		LanguageDetector languageDetector = new LanguageDetector() {
			@Override
			public String detect(SiteExtraction siteExtraction) {
				return "ko";
			}
		};
		SiteReader siteReader = new SiteReader(null, new ArticleReader(articleParser), feedParser, siteParser, languageDetector);
		return siteReader;
	}
	
    @Test
    public void testReadSite() throws IOException, ParserException {
        URL siteLocation = new URL("http://www.test.com/com/cee/news/parser/impl/spiegel.html");
        WebClient webClient = new ClassResourceWebClient();
        SiteReader siteReader = createSiteReader();
        
        Site site = siteReader.readSite(webClient, siteLocation.toExternalForm());
        assertEquals("SPIEGEL ONLINE - Nachrichten", site.getTitle());
        assertTrue(site.getDescription().startsWith("Deutschlands f"));
        assertEquals("ko", site.getLanguage());
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
        Mockito.when(response.openReader()).thenReturn(reader);
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
}
