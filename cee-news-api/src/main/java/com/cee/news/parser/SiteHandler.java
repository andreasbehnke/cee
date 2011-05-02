package com.cee.news.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cee.news.model.Feed;
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

    private enum States {
        start, header, finished
    };

    private States state = States.start;

    private final URL siteLocation;
    
    private final Site site = new Site();

    private final StringBuilder characterBuffer = new StringBuilder();

    public SiteHandler(final URL base) {
        this.siteLocation = base;
        this.site.setLocation(base.toExternalForm());
    }

    public Site getSite() {
        return site;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        characterBuffer.setLength(0);// reset the character buffer
        switch (state) {
        case start:
            if (localName.equalsIgnoreCase("head")) {
                state = States.header;
            }
            break;
        case header:
            if (localName.equalsIgnoreCase("link")) {
                String rel = attributes.getValue("rel");
                if (rel != null && rel.equalsIgnoreCase("alternate")) {
                    String title = attributes.getValue("title");
                    String type = attributes.getValue("type");
                    String href = attributes.getValue("href");
                    if (href != null) {
                        URL location = null;
                        try {
                            location = new URL(siteLocation, href);
                        } catch (MalformedURLException e) {
                            break;// the URL is invalid, ignore feed
                        }
                        site.getFeeds().add(new Feed(location.toExternalForm(), title, type));
                    }
                }
            } else if (localName.equalsIgnoreCase("meta")) {
                String name = attributes.getValue("name");
                if (name != null && name.equalsIgnoreCase("description")) {
                    site.setDescription(attributes.getValue("content"));
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
            if (localName.equalsIgnoreCase("head")) {
                state = States.finished;
                // TODO: what is the best practice to stop the XMLReader from
                // parsing
                // further HTML code? Documentation says, an exception should be
                // thrown...
            } else if (localName.equalsIgnoreCase("title")) {
                site.setTitle(characterBuffer.toString());
            }
        }
        characterBuffer.setLength(0);// reset the character buffer
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO what is the best way to handle this character chunks?
        characterBuffer.append(ch, start, length);
    }
}