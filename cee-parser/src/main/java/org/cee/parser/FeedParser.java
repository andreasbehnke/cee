package org.cee.parser;

/*
 * #%L
 * Content Extraction Engine - News Parser
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.cee.store.article.Article;
import org.cee.store.site.Feed;

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