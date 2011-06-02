package com.cee.news.parser;

import java.io.IOException;

import com.cee.news.model.Article;

public interface ArticleParser {

    /**
     * Parses the article content page and adds the content {@link TextBlock}s to the article.
     * @param article The article to be parsed
     * @return The article with new content added
     * @throws ParserException If the source could not be parsed
     * @throws IOException If an IO error occurred
     */
    Article parse(Article article) throws ParserException, IOException;

}