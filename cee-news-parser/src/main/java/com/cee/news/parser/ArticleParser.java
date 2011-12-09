package com.cee.news.parser;

import java.io.IOException;
import java.util.List;

import com.cee.news.model.Article;

public interface ArticleParser {
    
    /**
     * @return The list of filters which will be used to filter articles with poor content quality
     */
    List<ArticleFilter> getFilters();
    
    void setFilters(List<ArticleFilter> filters);

    /**
     * Parses the article content page and adds the content {@link TextBlock}s to the article.
     * @param article The article to be parsed
     * @return The article with new content added or null if the article content has a poor quality
     * @throws ParserException If the source could not be parsed
     * @throws IOException If an IO error occurred
     */
    Article parse(Article article) throws ParserException, IOException;

}