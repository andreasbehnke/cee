package com.cee.news.parser.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cee.news.model.Site;

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
	
	private static final String CONTENT_ATTRIBUTE = "content";

	private static final String DESCRIPTION_ATTRIBUTE = "description";

	private static final String NAME_ATTRIBUTE = "name";

	private static final String META_ELEMENT = "meta";

	private static final String HREF_ATTRIBUTE = "href";

	private static final String TYPE_ATTRIBUTE = "type";

	private static final String TITLE_ATTRIBUTE = "title";

	private static final String ALTERNATE_CONTENT = "alternate";

	private static final String REL_ATTRIBUTE = "rel";

	private static final String LINK_ELEMENT = "link";

	private static final String HEAD_ELEMENT = "head";

	private static final Logger LOG = LoggerFactory.getLogger(SiteHandler.class);

    private enum States {
        start, header, finished
    };

    private States state = States.start;

    private final URL siteLocation;
    
    private final Site site = new Site();
    
    private final List<String> feedLocations;

    private final StringBuilder characterBuffer = new StringBuilder();

    public SiteHandler(final URL base) {
        this.siteLocation = base;
        this.site.setLocation(base.toExternalForm());
        feedLocations = new ArrayList<String>();
    }

    public Site getSite() {
        return site;
    }
    
    public List<String> getFeedLocations() {
    	return feedLocations;
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
            if (localName.equalsIgnoreCase(LINK_ELEMENT)) {
                String rel = attributes.getValue(REL_ATTRIBUTE);
                if (rel != null && rel.equalsIgnoreCase(ALTERNATE_CONTENT)) {
                    String href = attributes.getValue(HREF_ATTRIBUTE);
                    if (href != null) {
                    	if (LOG.isDebugEnabled()) {
                    		String title = attributes.getValue(TITLE_ATTRIBUTE);
                            String type = attributes.getValue(TYPE_ATTRIBUTE);
                            LOG.debug("found feed {} of type {} at {}", new Object[]{title, type, href});
                    	}
                        URL location = null;
                        try {
                            location = new URL(siteLocation, href);
                        } catch (MalformedURLException e) {
                        	LOG.warn("found feed with invalid url: {}", href);
                            break;// the URL is invalid, ignore feed
                        }
                        feedLocations.add(location.toExternalForm());
                    }
                }
            } else if (localName.equalsIgnoreCase(META_ELEMENT)) {
                String name = attributes.getValue(NAME_ATTRIBUTE);
                if (name != null && name.equalsIgnoreCase(DESCRIPTION_ATTRIBUTE)) {
                	LOG.debug("found sites description");
                    site.setDescription(attributes.getValue(CONTENT_ATTRIBUTE));
                }
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (state == States.header) {
            if (localName.equalsIgnoreCase(HEAD_ELEMENT)) {
                state = States.finished;
                LOG.debug("finished document");
                // TODO: what is the best practice to stop the XMLReader from
                // parsing
                // further HTML code? Documentation says, an exception should be
                // thrown...
            } else if (localName.equalsIgnoreCase(TITLE_ATTRIBUTE)) {
            	LOG.debug("found sites title");
                site.setTitle(characterBuffer.toString());
            }
        }
        characterBuffer.setLength(0);// reset the character buffer
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characterBuffer.append(ch, start, length);
    }
}