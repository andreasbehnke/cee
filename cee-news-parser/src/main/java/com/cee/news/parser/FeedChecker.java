package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;

import com.cee.news.model.Feed;

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
    
    /**
     * @param feedLocation The location of the feed
     * @return Feed object containing all meta information about the feed
     * @throws IOException if an IO error occurred
     */
    public Feed parse(URL feedLocation) throws IOException;
    
}
