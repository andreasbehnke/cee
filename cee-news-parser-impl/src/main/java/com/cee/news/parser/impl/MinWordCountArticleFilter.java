package com.cee.news.parser.impl;

import com.cee.news.model.Article;
import com.cee.news.model.TextBlock;
import com.cee.news.parser.ArticleFilter;

/**
 * Article must have at least min words to be accepted.
 */
public class MinWordCountArticleFilter implements ArticleFilter {

    private static final int DEFAULT_MIN_WORD_COUNT = 10;
    
    private long minWordCount = DEFAULT_MIN_WORD_COUNT;

    /**
     * @return The minimum number of words an article should have
     */
    public long getMinWordCount() {
        return minWordCount;
    }

    public void setMinWordCount(long minWordCount) {
        this.minWordCount = minWordCount;
    }

    @Override
    public boolean accept(Article article) {
        long wordCount = 0;
        if (article.getContent() == null) {
            return false;
        }
        for (TextBlock text : article.getContent()) {
            wordCount += text.getNumWords();
        }
        return wordCount >= minWordCount;
    }
    
}
