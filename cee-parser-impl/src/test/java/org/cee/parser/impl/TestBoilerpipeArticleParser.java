package org.cee.parser.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
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


import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.cee.parser.ArticleParser;
import org.cee.parser.ParserException;
import org.cee.parser.ArticleParser.Settings;
import org.cee.parser.impl.BoilerpipeArticleParser;
import org.cee.parser.impl.TagsoupXmlReaderFactory;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.impl.DefaultHttpClientFactory;
import org.cee.parser.net.impl.DefaultWebClient;
import org.cee.parser.net.impl.XmlStreamReaderFactory;
import org.cee.store.article.Article;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBoilerpipeArticleParser {
	
	private final static Logger LOG = LoggerFactory.getLogger(TestBoilerpipeArticleParser.class);
	
	private String getContent(String articleTitle, URL articleLocation) throws ParserException, IOException {
		Article article = new Article();
		article.setTitle(articleTitle);
        article.setLocation(articleLocation.toExternalForm());
        ArticleParser parser = new BoilerpipeArticleParser(new TagsoupXmlReaderFactory());
        WebClient webClient = new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory());
        Reader reader = webClient.openWebResponse(articleLocation).openReader();
        try {
        	parser.parse(reader, article, new Settings());
        } finally {
        	IOUtils.closeQuietly(reader);
        }
        return article.getContentText();
	}
	
	private boolean testExpectedContent(String articleTitle, URL articleLocation, String... expectedContents) throws ParserException, IOException {
		String content = getContent(articleTitle, articleLocation);
		boolean result = true;
        for (String expectedContent : expectedContents) {
			if (!content.contains(expectedContent)) {
				LOG.error("expected text: {}", expectedContent);
				result = false;
			}
		}
        return result;
	}
	
	private boolean testUnexpectedContent(String articleTitle, URL articleLocation, String... unexpectedContents) throws ParserException, IOException {
		String content = getContent(articleTitle, articleLocation);
		boolean result = true;
		for (String unexpectedContent : unexpectedContents) {
			if (content.contains(unexpectedContent)) {
				LOG.error("Not expected text: {}", unexpectedContent);
				result = false;
			}
		}
        return result;
	}
	
    @Test
    public void testParse() throws ParserException, IOException {
    	assertTrue(testExpectedContent(
    			"Russischer Ölkonzern Rosneft steigt bei BP ein",
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
				"Troika sieht Portugal auf gutem Weg",
				getClass().getResource("issue146.html"), 
				"Die Portugiesen selbst sind weniger optimistisch"));
	}
	
	
    @Test
    public void testParseRegressionIssue205() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Obama schaltet Werbespots in Pakistan",
				getClass().getResource("issue205.html"), 
				"In dem 52 Sekunden kurzen und mit Urdu-Untertiteln versehenen Beitrag"));
    }
    
    @Test
    public void testParseRegressionIssue206() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Nach Freitagsgebeten Tote bei antiwestlichen Protesten in Pakistan",
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
				"Hisbollah bekennt sich zu Drohneneinsatz",
				getClass().getResource("issue212.html"), 
				"Die libanesische Hisbollah erklärte, sie habe das Flugobjekt zu Spionagezwecken eingesetzt"));
	}

	@Test
	public void testParseRegressionIssue214() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				"Apple-Preiserhöhung betrifft auch In-App-Käufe",
				getClass().getResource("issue214.html"), 
				"Am 26. Oktober kommt Windows 8 in den Handel"));
	}
	
	@Test
	public void testParseRegressionIssue215() throws ParserException, IOException {
		assertTrue(testUnexpectedContent(
				"Ex-Partner von Zuckerberg wegen Milliardenbetrugs verhaftet",
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
		assertTrue(testExpectedContent(
				"Niederlage auf Schalke: Nürnberg-Fans belagern Mannschaftsbus",
				getClass().getResource("issue217.html"), 
				"Die Lage beruhigte sich erst nach dem Eingreifen der Polizei und von"));
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
	
	@Test
	public void testParseRegressionIssue279() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Zu viel Angst vor der Geburt",
				getClass().getResource("issue279.html"), 
				"Etwa fünf Prozent der Schwangeren fürchten sich extrem vor einer natürlichen Geburt.",
				"Unnötige Kaiserschnitte zu vermeiden, sei schon deshalb wünschenswert, weil es durch diese OP öfter zu Komplikationen wie Blutungen, Infektionen und Stillproblemen komme, sagte Utz-Billing."));
	}
	
	@Test
	public void testParseRegressionIssue280() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Oliver Welke: „Ich bin irre, ich weiß“",
				getClass().getResource("issue280.html"), 
				"Günter Netzer, größter noch lebender Fußballexperte, soll gesagt haben, es gebe im Leben Wichtigeres, als gegen einen Fußball zu treten. Der Mann muss verrückt geworden sein.",
				" Alle, die irgendwann ein Mikrofon in der Hand gehalten haben, hören sich danach anders an. "));
	}
	
	@Test
	public void testParseRegressionIssue281() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Nato-Aufklärungsdrohne: Abgeordnete fordern Global-Hawk-Beschaffung zu prüfen",
				getClass().getResource("issue281.html"), 
				"hinsichtlich des nationalen Drohenprojekts „Euro Hawk“",
				"De Maizière (CDU) hatte am Freitag den Vorwurf zurückgewiesen, das Drohnenprojekt „Euro Hawk“ zu spät gestoppt zu haben."));
	}
	
	@Test
	public void testParseRegressionIssue282() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Soldat bei Messerattacke in Paris verletzt",
				getClass().getResource("issue282.html"), 
				"In London wurde außerdem ein Freund eines der beiden mutmaßlichen Täter festgenommen."));
	}
	
	@Test
	public void testParseRegressionIssue283() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"In Istanbul sind der Fotograf Jim Rakete und der Lyriker Gerhard Falkner Freunde geworden",
				getClass().getResource("issue283.html"), 
				"Der Fotograf Jim Rakete und der Lyriker Gerhard Falkner leben beide in Berlin.",
				"Die Kulturakademie Tarabya hat einen doppelten Boden. Man ist in Istanbul und ist es nicht.",
				"Falkner kneift die Augen zusammen, Rakete rückt seine Sonnenbrille zurecht."));
	}
	
	@Test
	public void testParseRegressionIssue284() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Fangmengen: EU will Fischerei beschränken",
				getClass().getResource("issue284.html"), 
				"Meere sollen besser",
				"erlaubt sein\", teilte die irische",
				"Generell hatte das Parlament eine ehrgeizigere Linie verfochten"));
	}

	@Test
	public void testParseRegressionIssue287() throws ParserException, IOException {
		assertTrue(testExpectedContent(
				"Armutsbekämpfung bei der UN : Extreme Armut bis 2030 beseitigen",
				getClass().getResource("issue287.html"), 
				"Mit einem ehrgeizigen Fahrplan",
				"Die Politiker stehen einem Gremium aus 27 Regierungsmitgliedern und",
				"Wachstum und eine Verdopplung des Anteils erneuerbarer Energien"));
	}
}
