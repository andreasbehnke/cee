package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.Feed;

/**
 * Fetches all articles of a feed. This parser will not retrieve the article content,
 * only publication date, title and description (abstract, short text) will be parsed.
 */
public interface FeedParser {

    /**
     * Read all articles from syndication feed
     * @param feedLocation The URL of the feed
     * @return List of articles, article title, date and description are set.
     * @throws ParserException If the feed could not be parsed
     * @throws IOException If the feeds stream could not be opened
     */
    List<Article> readArticles(final URL feedLocation) throws ParserException, IOException;

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