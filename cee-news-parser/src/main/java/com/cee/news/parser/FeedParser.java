package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.cee.news.model.Article;

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
    List<Article> parse(final URL feedLocation) throws ParserException, IOException;

}