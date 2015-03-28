package org.cee.parser.impl.html;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Internal handler class which will be registered with the XMLReader for
 * reading the feed and meta information of a web-site:
 * <ol>
 * <li>The HTML title elements content is mapped to the property title.</li>
 * <li>The content of the meta element named "description" is mapped to the property description.</li>
 * <li>For each link element with the rel attribute set to "alternative" a feed is created.</li>
 * </ol>
 */
public class SiteHandler extends DefaultHandler {
	
	private static final String BODY_ELEMENT = "body";

	private static final String CONTENT_ATTRIBUTE = "content";

	private static final String DESCRIPTION_ATTRIBUTE = "description";

	private static final String TITLE_ELEMENT = "title";

	private static final String NAME_ATTRIBUTE = "name";

	private static final String META_ELEMENT = "meta";

	private static final String HEAD_ELEMENT = "head";

	private static final Logger LOG = LoggerFactory.getLogger(SiteHandler.class);

    private enum States {
        start, header, body, finished
    };

    private States state = States.start;

    private final StringBuilder characterBuffer = new StringBuilder();
    
    private String title;
    
    private String description;
    
    private final StringBuilder content = new StringBuilder();
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public StringBuilder getContent() {
        return content;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        characterBuffer.setLength(0);// reset the character buffer
        switch (state) {
        case start:
            if (localName.equalsIgnoreCase(HEAD_ELEMENT)) {
            	LOG.debug("found document head");
                state = States.header;
            }
            break;
        case header:
            if (localName.equalsIgnoreCase(META_ELEMENT)) {
                String name = attributes.getValue(NAME_ATTRIBUTE);
                if (name != null && name.equalsIgnoreCase(DESCRIPTION_ATTRIBUTE)) {
                	LOG.debug("found sites description");
                	this.description = attributes.getValue(CONTENT_ATTRIBUTE);
                }
            } else if (localName.equalsIgnoreCase(BODY_ELEMENT)) {
            	LOG.debug("found document body");
                state = States.body;
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (state == States.header && localName.equalsIgnoreCase(TITLE_ELEMENT)) {
            LOG.debug("found sites title");
            this.title = characterBuffer.toString();
        } else if (state == States.body) {
        	if (localName.equalsIgnoreCase(BODY_ELEMENT)) {
            	state = States.finished;
                LOG.debug("finished document");
        	}
        	this.content.append(characterBuffer).append('\n');
        }
        characterBuffer.setLength(0);// reset the character buffer
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characterBuffer.append(ch, start, length);
    }
}