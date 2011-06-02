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
     * @param feedLocation
     * @return
     * @throws ParserException
     * @throws IOException
     */
    List<Article> parse(final URL feedLocation) throws ParserException, IOException;

}