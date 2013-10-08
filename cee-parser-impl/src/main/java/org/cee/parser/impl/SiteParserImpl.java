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


import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.cee.SiteExtraction;
import org.cee.language.LanguageDetector;
import org.cee.parser.ParserException;
import org.cee.parser.SiteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Extracts feed URLs from the header of a HTML document.
 * Sets the title and the description of the created site using
 * the meta information from document header. Detects site language
 * using {@link LanguageDetector}.
 */
public class SiteParserImpl extends XmlReaderProvider implements SiteParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteParserImpl.class);

    public SiteParserImpl() {}
    
    public SiteParserImpl(SaxXmlReaderFactory xmlReaderFactory) {
        super(xmlReaderFactory);
    }

    /**
     * Parses the HTML content and extracts all feeds using the provided reade;r.
     * The feed instances are created by the feedService for each LINK element
     * with a rel attribute set to "alternate".
     * 
     * @param siteLocation
     *            The URL of the content to be parsed
     * @return The site parsed from the given location
     * @throws IOException
     *             If the stream could not be opened
     * @throws SAXException
     *             If the input source could not be parsed
     */
    @Override
    public SiteExtraction parse(Reader reader, URL siteLocation) throws IOException, ParserException {
        XMLReader xmlReader = createXmlReader();
    	SiteHandler siteHandler = new SiteHandler(siteLocation);
        xmlReader.setContentHandler(siteHandler);
        try {
        	InputSource is = new InputSource(reader);
        	LOG.info("start parsing site document {}", siteLocation);
            xmlReader.parse(is);
        } catch (SAXException e) { 
        	throw new ParserException("Could not parse site", e);
        } finally {
        	LOG.info("finished parsing site document {}", siteLocation);
        	IOUtils.closeQuietly(reader);
        }
        return siteHandler.getSiteExtraction();
    }
}
