package com.cee.news.parser.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import betamax.Betamax;
import betamax.Recorder;
import betamax.TapeMode;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.impl.DefaultHttpClientFactory;
import com.cee.news.parser.net.impl.DefaultWebClient;
import com.cee.news.parser.net.impl.XmlStreamReaderFactory;

public class TestBoilerpipeArticleParser {
	
	@Rule
	public Recorder recorder = new Recorder();

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

	@Betamax(tape = "issue120", mode = TapeMode.READ_ONLY)
	@Test
	public void testParseRegressionIssue120() throws ParserException, IOException {
		Article article = new Article();
        article.setLocation("http://www.swr.de/nachrichten/-/id=396/nid=396/did=8825882/1u2s8qj/index.html");
        
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory()));
        parser.parse(article);
        assertTrue(article.getContentText().contains("Kein Referendum, kein Rücktritt, keine Lösung"));
	}
	
	@Betamax(tape = "issue144", mode = TapeMode.READ_ONLY)
	@Test
	public void testParseRegressionIssue144() throws ParserException, IOException {
		Article article = new Article();
        article.setLocation("http://www.spiegel.de/wirtschaft/unternehmen/0,1518,797729,00.html");
        
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory()));
        parser.parse(article);
        assertTrue(article.getContentText().contains("die Polizei durchsucht das Büro"));
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
	
	@Ignore("The server does not send a UTF-8 Content-Type header, betamax is also unable to detect the charset encoding...")
	//@Betamax(tape = "issue145", mode = TapeMode.READ_WRITE)
	@Test
	public void testParseRegressionIssue145() throws ParserException, IOException {
		Article article = new Article();
        article.setLocation("http://www.swr.de/nachrichten/-/id=396/nid=396/did=8892142/1aevpcg/index.html");
        
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory()));
        parser.parse(article);
        assertTrue(article.getContent().get(0).getContent().contains("täglich"));
	}
	
	@Test
	public void testParseRegressionIssue146() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue146.html"), 
				"Die Portugiesen selbst sind weniger optimistisch"));
	}
	
	@Test
	public void testParseRegressionIssue212() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				getClass().getResource("issue212.html"), 
				"Die libanesische Hisbollah erklärte, sie habe das Flugobjekt zu Spionagezwecken eingesetzt"));
	}
}
