package com.cee.news.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import betamax.Betamax;
import betamax.Recorder;
import betamax.TapeMode;

import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.FeedChecker;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.net.ClassResourceWebClient;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.parser.net.impl.DefaultHttpClientFactory;
import com.cee.news.parser.net.impl.DefaultWebClient;
import com.cee.news.parser.net.impl.XmlStreamReaderFactory;

public class TestSiteParser {
    
    @Rule
    public Recorder recorder = new Recorder();
    
    @Test
    public void testParse() throws IOException, SAXException {
        URL siteLocation = new URL("http://www.test.com/com/cee/news/parser/impl/spiegel.html");
        WebClient webClient = new ClassResourceWebClient();
        SiteParser parser = new SiteParser(new Parser(), new RomeFeedChecker(webClient), webClient);
        Site site = parser.parse(siteLocation);
        assertEquals("SPIEGEL ONLINE - Nachrichten", site.getTitle());
        assertTrue(site.getDescription().startsWith("Deutschlands f"));
        assertEquals(2, site.getFeeds().size());
        Feed feed = site.getFeeds().get(0);
        assertEquals("SPIEGEL ONLINE RSS Schlagzeilen", feed.getTitle());
        assertEquals("application/rss+xml", feed.getContentType());
        assertEquals(new URL(siteLocation, "spiegelSchlagzeilen.rss").toExternalForm(), feed.getLocation());
        feed = site.getFeeds().get(1);
        assertEquals("SPIEGEL ONLINE RSS Nachrichten", feed.getTitle());
        assertEquals("application/rss+xml", feed.getContentType());
        assertEquals(new URL(siteLocation, "spiegelNachrichten.rss").toExternalForm(), feed.getLocation());

    }

    @Test
    public void testParseRegressionIssue190() throws ParserException, IOException, SAXException {
        URL siteLocation = new URL("http://www.faz.de");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Reader reader = new InputStreamReader(inputStream);
        WebResponse response = Mockito.mock(WebResponse.class);
        Mockito.when(response.getLocation()).thenReturn(new URL("http://www.faz.net"));
        Mockito.when(response.getStream()).thenReturn(inputStream);
        Mockito.when(response.getReader()).thenReturn(reader);
        WebClient webClient = Mockito.mock(WebClient.class);
        Mockito.when(webClient.openWebResponse(siteLocation)).thenReturn(response);
        
        SiteParser parser = new SiteParser(new Parser(), new RomeFeedChecker(webClient), webClient);
        Site site = parser.parse(siteLocation);
        assertEquals(site.getLocation(), "http://www.faz.net");
    }
    
    @Betamax(tape = "issue202", mode = TapeMode.READ_WRITE)
    @Test
    public void testParseRegressionIssue202() throws MalformedURLException, ParserException, IOException, SAXException {
        WebClient webClient = new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory());
        FeedChecker feedChecker = new RomeFeedChecker(webClient);
        SiteParser parser = new SiteParser(new Parser(), feedChecker, webClient);
        
        Site site = parser.parse(new URL("http://www.cnn.com/"));
        assertEquals(2, site.getFeeds().size());
    }
}
