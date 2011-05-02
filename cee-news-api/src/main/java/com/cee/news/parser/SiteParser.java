package com.cee.news.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.net.WebClient;

/**
 * Extracts feed URLs from the header of a HTML document.
 * Sets the title and the description of the created site using
 * the meta information from document header.
 */
public class SiteParser {

    private WebClient webClient;
    
    private XMLReader reader;
    
    private FeedChecker feedChecker;

    /**
     * {@link XMLReader} used by the parser.
     * @param reader
     *            The XMLReader instance used to parse HTML content
     */
    public void setReader(XMLReader reader) {
        this.reader = reader;
    }
    
    /**
     * @param feedChecker {@link FeedChecker} instance used to test, if an URL points to a supported feed type
     */
    public void setFeedChecker(FeedChecker feedChecker) {
        this.feedChecker = feedChecker;
    }
    
    /**
     * @param webClient The web client used for web connections
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public SiteParser() {}
    
    public SiteParser(XMLReader reader, FeedChecker feedChecker, WebClient webClient) {
        this.reader = reader;
        this.feedChecker = feedChecker;
        this.webClient = webClient;
    }

    /**
     * Parses the HTML content and extracts all feeds using the provided reader.
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
    public Site parse(URL siteLocation) throws IOException, SAXException {
        if (reader == null) {
            throw new IllegalStateException("reader property has not been set");
        }
        if (feedChecker == null) {
            throw new IllegalStateException("feedChecker property has not been set");
        }

        SiteHandler siteHandler = new SiteHandler(siteLocation);
        reader.setContentHandler(siteHandler);

        InputStream input = webClient.openStream(siteLocation);
        try {
            reader.parse(new InputSource(input));
        } finally {
            input.close();
        }
        
        //remove feeds with unknown content type
        Site site = siteHandler.getSite();
        List<Feed> feeds = site.getFeeds();
        List<Feed> remove = new ArrayList<Feed>();
        for (Feed feed : feeds) {
            if (!feedChecker.isSupportedFeed(new URL(feed.getLocation()))) {
                remove.add(feed);
            }
        }
        feeds.removeAll(remove);
        
        return site;
    }
}
