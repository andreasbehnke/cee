package com.cee.news.parser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.impl.DefaultHttpClientFactory;
import com.cee.news.parser.net.impl.DefaultWebClient;
import com.cee.news.parser.net.impl.XmlStreamReaderFactory;

public class TestBoilerpipeArticleParser {
	
	private boolean testExpectedContent(URL articleLocation, String... expectedContents) throws ParserException, IOException {
		Article article = new Article();
        article.setLocation(articleLocation.toExternalForm());
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory()));
        parser.parse(article);
        String content = article.getContentText();
        for (String expectedContent : expectedContents) {
			if (!content.contains(expectedContent)) {
				return false;
			}
		}
        return true;
	}
	
    @Test
    public void testParse() throws ParserException, IOException {
    	assertTrue(testExpectedContent(
				getClass().getResource("spiegelArticle.html"), 
				"Russischer "));
    }
	
	@Test
	public void testParseRegressionIssue144() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue144.html"), 
				"die Polizei durchsucht das Büro"));
	}
	
	@Test
	public void testParseRegressionIssue146() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue146.html"), 
				"Die Portugiesen selbst sind weniger optimistisch"));
	}
	
	
    @Test
    public void testParseRegressionIssue205() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue205.html"), 
				"In dem 52 Sekunden kurzen und mit Urdu-Untertiteln versehenen Beitrag"));
    }
    
    @Test
    public void testParseRegressionIssue206() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue206.html"),
				//first article half
				"Mehrere westliche Staaten haben aus Furcht vor Ausschreitungen ihre Botschaften in islamischen Ländern geschlossen",
				"Nach ersten Meldungen wurden dabei mindestens 15 Menschen getötet und mehr als 160 verletzt",
				"Gesetze gegen die Beleidigung des Propheten zu erlassen",
				//second article half
				"In mehreren Werbefilmen, die am Freitag im pakistanischen Fernsehen ausgestrahlt wurden, distanzieren sich der amerikanische Präsident Barack Obama und Außenministerin Hillary Clinton von dem islamfeindlichen Film",
				"Das Innenministerium in Tunis hatte zuvor Demonstrationen gegen den Film verboten"));
    }
	
	@Test
	public void testParseRegressionIssue212() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue212.html"), 
				"Die libanesische Hisbollah erklärte, sie habe das Flugobjekt zu Spionagezwecken eingesetzt"));
	}

	@Test
	public void testParseRegressionIssue214() throws ParserException, IOException {
		assertFalse(testExpectedContent(
				getClass().getResource("issue214.html"), 
				"Am 26. Oktober kommt Windows 8 in den Handel"));
	}
}
