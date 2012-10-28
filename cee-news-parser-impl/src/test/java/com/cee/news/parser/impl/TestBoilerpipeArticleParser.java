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
	
	private String getContent(String articleTitle, URL articleLocation) throws ParserException, IOException {
		Article article = new Article();
		article.setTitle(articleTitle);
        article.setLocation(articleLocation.toExternalForm());
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory()));
        parser.parse(article);
        return article.getContentText();
	}
	
	private boolean testExpectedContent(String articleTitle, URL articleLocation, String... expectedContents) throws ParserException, IOException {
		String content = getContent(articleTitle, articleLocation);
        for (String expectedContent : expectedContents) {
			if (!content.contains(expectedContent)) {
				return false;
			}
		}
        return true;
	}
	
	private boolean testUnexpectedContent(String articleTitle, URL articleLocation, String... unexpectedContents) throws ParserException, IOException {
		String content = getContent(articleTitle, articleLocation);
        for (String unexpectedContent : unexpectedContents) {
			if (content.contains(unexpectedContent)) {
				return false;
			}
		}
        return true;
	}
	
    @Test
    public void testParse() throws ParserException, IOException {
    	assertTrue(testExpectedContent(
    			null,
				getClass().getResource("spiegelArticle.html"), 
				"Tony Hayward"));
    }
	
	@Test
	public void testParseRegressionIssue144() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Der verpatzte Abgang des Josef Ackermann",
				getClass().getResource("issue144.html"), 
				"die Polizei durchsucht das Büro"));
	}
	
	@Test
	public void testParseRegressionIssue146() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				null,
				getClass().getResource("issue146.html"), 
				"Die Portugiesen selbst sind weniger optimistisch"));
	}
	
	
    @Test
    public void testParseRegressionIssue205() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				null,
				getClass().getResource("issue205.html"), 
				"In dem 52 Sekunden kurzen und mit Urdu-Untertiteln versehenen Beitrag"));
    }
    
    @Test
    public void testParseRegressionIssue206() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				null,
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
				null,
				getClass().getResource("issue212.html"), 
				"Die libanesische Hisbollah erklärte, sie habe das Flugobjekt zu Spionagezwecken eingesetzt"));
	}

	@Test
	public void testParseRegressionIssue214() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				null,
				getClass().getResource("issue214.html"), 
				"Am 26. Oktober kommt Windows 8 in den Handel"));
	}
	
	@Test
	public void testParseRegressionIssue215() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				null,
				getClass().getResource("issue215.html"), 
				"Aus Datenschutzgründen wird Ihre IP-Adresse nur dann gespeichert"));
	}
	
	@Test
	public void testParseRegressionIssue216() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				"Sicherheitsupdate für Firefox und Thunderbird",
				getClass().getResource("issue216.html"), 
				"heise online > News > 2012 > KW 43 > Sicherheitsupdate für Firefox und Thunderbird",
				"27.10.2012 16:32",
				"« Vorige | Nächste »",
				"Sicherheitsupdate für Firefox und Thunderbird"));
	}
	
	@Test
	public void testParseRegressionIssue217() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				"Niederlage auf Schalke: Nürnberg-Fans belagern Mannschaftsbus",
				getClass().getResource("issue217.html"), 
				"Auf anderen Social Networks teilen",
				"Forumname",
				"alles aus der Rubrik Sport",
				"Fanflash"));
	}

	@Test
	public void testParseRegressionIssue218() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Israel: Unter Feuer",
				getClass().getResource("issue218.html"), 
				"Hebron sollen mehr als 1500",
				"auf dem Betonblock, der aus der wüstenhaften Hochebene",
				"Das Schicksal der Bewohner ist noch ungewiss",
				"Nach Ansicht des Verteidigungsministeriums",
				"bald kein Platz mehr, befürchtet Lecker"));
	}

	@Test
	public void testParseRegressionIssue219() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				"Eher kleine Wellen nach Tsunami-Warnung",
				getClass().getResource("issue219.html"), 
				"Erdbeben vor Kanadas Westküste",
				"Eher kleine Wellen nach Tsunami-Warnung"));
	}
	
	@Test
	public void testParseRegressionIssue220() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Googles Web Toolkit 2.5 mit schlankerem Code",
				getClass().getResource("issue220.html"), 
				"ein in Java geschriebenes quelloffenes Webframework für Ajax-Anwendungen",
				"GWT enthält als Besonderheit einen Java-nach-JavaScript-Compiler"));
	}
}
