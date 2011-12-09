package com.cee.news.parser;

import com.cee.news.model.Article;

/**
 * Filters articles based on their content.
 * Used by the {@link ArticleParser} for filtering articles with poor content quality
 */
public interface ArticleFilter {

    boolean accept(Article article);
    
}
