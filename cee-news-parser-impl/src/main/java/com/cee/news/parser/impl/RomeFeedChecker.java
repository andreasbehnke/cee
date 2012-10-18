package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.xml.sax.InputSource;

import com.cee.news.model.Feed;
import com.cee.news.parser.FeedChecker;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SAXBuilder;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.WireFeedParser;
import com.sun.syndication.io.impl.FeedParsers;
import com.sun.syndication.io.impl.XmlFixerReader;

public class RomeFeedChecker extends WireFeedInput implements FeedChecker {
    
    private static FeedParsers feedParsers;
    
    private WebClient webClient;
    
    public RomeFeedChecker() {
        synchronized (RomeFeedChecker.class) {
            if (feedParsers == null) {
                feedParsers = new FeedParsers();
            }
        }
    }
    
    public RomeFeedChecker(WebClient webClient) {
        this();
        this.webClient = webClient;
    }
    
    /**
     * @param webClient Client used to execute web requests
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    private Document openDocument(final URL feedLocation) throws JDOMException, IOException {
    	SAXBuilder saxBuilder = createSAXBuilder();
    	WebResponse response = webClient.openWebResponse(feedLocation);
        Document document = null;
        Reader reader = null;
        try {
        	reader = new XmlFixerReader(response.getReader());
        	document = saxBuilder.build(new InputSource(reader));
        } finally {
        	if (reader != null)
        		reader.close();
        }
        return document;
    }

    @Override
    public boolean isSupportedFeed(final URL feedLocation) throws IOException {
        Document document = null;
		try {
			document = openDocument(feedLocation);
		} catch (JDOMException e) {
			return false;
		}
        WireFeedParser parser = feedParsers.getParserFor(document);
        return parser != null;
    }
    
    @Override
    public Feed parse(URL feedLocation) throws IOException {
    	try {
    		Document document = openDocument(feedLocation);
    		SyndFeedInput input = new SyndFeedInput();
    		SyndFeed syndFeed = input.build(document);
    		return new Feed(feedLocation.toExternalForm(), syndFeed.getTitle(), null);
		} catch (JDOMException | IllegalArgumentException | FeedException e) {
			throw new IOException(e);
		}
    }
}
