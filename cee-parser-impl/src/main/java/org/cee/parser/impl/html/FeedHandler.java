package org.cee.parser.impl.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Internal handler class which will be registered with the XMLReader for
 * reading all alternate feeds of a web-site.
 */
public class FeedHandler extends DefaultHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeedHandler.class);

    private static final String HREF_ATTRIBUTE = "href";

    private static final String TYPE_ATTRIBUTE = "type";

    private static final String TITLE_ATTRIBUTE = "title";

    private static final String ALTERNATE_CONTENT = "alternate";

    private static final String REL_ATTRIBUTE = "rel";

    private static final String LINK_ELEMENT = "link";
    
    private final Set<URL> feedLocations = new HashSet<>();
    
    private final URL baseUrl;
    
    public FeedHandler(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public Set<URL> getFeedLocations() {
        return feedLocations;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
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
                        location = new URL(baseUrl, href);
                        feedLocations.add(location);
                    } catch (MalformedURLException e) {
                        LOG.warn("found feed with invalid url: {}", href);
                    }
                }
            }
        }
    }
}
