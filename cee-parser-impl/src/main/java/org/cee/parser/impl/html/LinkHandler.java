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
 * Extracts all links from HTML page
 */
public class LinkHandler extends DefaultHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeedHandler.class);

    private static final String HREF_ATTRIBUTE = "href";

    private static final String A_ELEMENT = "a";
    
    private static final String JAVASCRIPT = "javascript:";
    
    private final Set<URL> links = new HashSet<>();
    
    private final URL baseUrl;
    
    public LinkHandler(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public Set<URL> getLinks() {
        return links;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equalsIgnoreCase(A_ELEMENT)) {
            String href = attributes.getValue(HREF_ATTRIBUTE);
            if (href != null && !href.startsWith(JAVASCRIPT)) {
                LOG.debug("found link to {}", new Object[]{href});
                URL location = null;
                try {
                    location = new URL(baseUrl, href);
                    links.add(location);
                } catch (MalformedURLException e) {
                    LOG.warn("found link with invalid url: {}", href);
                }
            }
        }
    }
}
