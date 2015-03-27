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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.SiteExtraction;
import org.cee.net.WebClient;
import org.cee.net.impl.ClassResourceWebClient;
import org.cee.parser.ParserException;
import org.cee.store.site.Site;
import org.junit.Test;

public class TestSiteParser {
	
	private SiteExtraction readSite(URL siteLocation) throws IOException, ParserException {
		WebClient webClient = new ClassResourceWebClient();
        SiteParserImpl parser = new SiteParserImpl(new TagsoupXmlReaderFactory());
        try (Reader reader = webClient.openWebResponse(siteLocation, false).openReader()) {
        	return parser.parse(reader, siteLocation);
        }
	}
    
    @Test
    public void testParse() throws IOException, ParserException {
        URL siteLocation = new URL("http://www.test.com/org/cee/parser/impl/spiegel.html");
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
