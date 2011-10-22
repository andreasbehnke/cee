package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;

/**
 * Checks if a given URL points to a supported content syndication type
 */
public interface FeedChecker {

    /**
     * @param feedLocation The location of the feed
     * @return true, if the feed is supported
     * @throws IOException If an IO error occurred while reading remote location
     */
    public boolean isSupportedFeed(URL feedLocation) throws IOException;
    
}
