package com.cee.news.parser.impl;

import java.io.IOException;
import java.net.URL;

import org.jdom.Document;
import org.jdom.JDOMException;

import com.cee.news.model.Feed;
import com.cee.news.parser.FeedChecker;
import com.cee.news.parser.net.WebClient;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.WireFeedParser;
import com.sun.syndication.io.impl.FeedParsers;

public class RomeFeedChecker extends RomeFeedBase implements FeedChecker {
    
    private static FeedParsers feedParsers;
    
    public RomeFeedChecker() {
        synchronized (RomeFeedChecker.class) {
            if (feedParsers == null) {
                feedParsers = new FeedParsers();
            }
        }
    }
    
    public RomeFeedChecker(WebClient webClient) {
        this();
        setWebClient(webClient);
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
