package com.cee.news.parser;

import java.io.IOException;
import java.io.Reader;
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
     * @return List of articles, article title, date and description are set.
     * @throws ParserException If the feed could not be parsed
     * @throws IOException If the feeds stream could not be opened
     */
    List<Article> readArticles(Reader input, URL feedLocation) throws ParserException, IOException;

	/**
	 * @return Feed object containing all meta information about the feed
	 * @throws IOException if an IO error occurred
	 * @throws ParserException if feed is not supported
	 */
	public Feed parse(Reader input, URL feedLocation) throws ParserException, IOException;

}