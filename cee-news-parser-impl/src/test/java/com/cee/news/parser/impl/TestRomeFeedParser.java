package com.cee.news.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.impl.DefaultHttpClientFactory;
import com.cee.news.parser.net.impl.DefaultWebClient;
import com.cee.news.parser.net.impl.XmlStreamReaderFactory;

public class TestRomeFeedParser {
	
	private FeedParser createFeedParser() {
		return new RomeFeedParser(new Parser());
	}
	
	private Reader openReader(URL location) throws IOException {
		WebClient webClient = new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory());
        return webClient.openReader(location);
	}
	
	private List<Article> readArticles(URL location) throws IOException, ParserException {
		FeedParser parser = createFeedParser();
		Reader reader = openReader(location);
		try {
			return parser.readArticles(reader, location);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	@Test
    public void testReadArticles() throws ParserException, IOException {
		List<Article> articles = readArticles(getClass().getResource("spiegelNachrichten.rss"));
        
        assertEquals(7, articles.size());
        Article article = articles.get(0);
        assertEquals("Krieg in Afghanistan: Nato startet Awacs-Mission ohne Deutschland", article.getTitle());
        assertEquals("http://www.spiegel.de/politik/ausland/0,1518,738566,00.html#ref=rss", article.getLocation().toString());
        assertTrue(article.getShortText().startsWith(
                "Die Nato beginnt die"));
        assertEquals(6, article.getPublishedDate().get(Calendar.HOUR));
        assertEquals("http://www.spiegel.de/politik/ausland/0,1518,738566,00.html", article.getExternalId());

    }
	

    @Test
    public void testReadHtmlShortText() throws ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("feedWithHtmlDescription.rss"));
        
        Article article = articles.get(0);
        assertEquals(
        		"Das Hochwasser stellt Teile Deutschlands weiter vor riesige Probleme. Mittlerweile rollt die Flutwelle gen Norddeutschland. Im Süden und Osten hinterlässt sie Zerstörung. In Sachsen-Anhalt kam ein weiterer Mensch ums Leben.",
        		article.getShortText());
    }
    
    @Test
    public void testReadArticlesRegressionIssue143() throws MalformedURLException, ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("issue143.xml"));
        assertEquals(42, articles.size());
    }
    
    @Test
    public void testReadArticlesRegressionIssue297() throws MalformedURLException, ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("issue297.xml"));
        assertNotNull(articles.get(0).getShortText());
        assertTrue(articles.get(0).getShortText().contains("Dresden (dpa/sn) - Dresden will"));
    }
}
