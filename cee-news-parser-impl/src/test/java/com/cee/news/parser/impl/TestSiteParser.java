package com.cee.news.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;

import com.cee.news.SiteExtraction;
import com.cee.news.model.Site;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.impl.ClassResourceWebClient;

public class TestSiteParser {
	
	private SiteExtraction readSite(URL siteLocation) throws IOException, ParserException {
		WebClient webClient = new ClassResourceWebClient();
        SiteParserImpl parser = new SiteParserImpl(new Parser());
        Reader reader = webClient.openReader(siteLocation);
        try {
        	return parser.parse(reader, siteLocation);
        } finally {
        	IOUtils.closeQuietly(reader);
        }
	}
    
    @Test
    public void testParse() throws IOException, ParserException {
        URL siteLocation = new URL("http://www.test.com/com/cee/news/parser/impl/spiegel.html");
        SiteExtraction siteExtraction = readSite(siteLocation);
        Site site = siteExtraction.getSite();
        assertEquals("SPIEGEL ONLINE - Nachrichten", site.getTitle());
        assertTrue(site.getDescription().startsWith("Deutschlands f"));
        assertEquals(2, siteExtraction.getFeedLocations().size());
        URL feedLocation = siteExtraction.getFeedLocations().get(0);
        assertEquals(new URL(siteLocation, "spiegelSchlagzeilen.rss"), feedLocation);
        feedLocation = siteExtraction.getFeedLocations().get(1);
        assertEquals(new URL(siteLocation, "spiegelNachrichten.rss"), feedLocation);

    }
}
