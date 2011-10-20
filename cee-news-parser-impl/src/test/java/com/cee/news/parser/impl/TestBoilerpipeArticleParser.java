package com.cee.news.parser.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.ClassResourceWebClient;

public class TestBoilerpipeArticleParser {

    private static final String ARTICLE_START_TEXT = "Russischer ";
	private static final String ARTICLE_LOCATION = "http://www.test.com/com/cee/news/parser/impl/spiegelArticle.html";

	@Test
    public void testParse() throws ParserException, IOException {
        Article article = new Article();
        article.setLocation(ARTICLE_LOCATION);
        ArticleParser parser = new BoilerpipeArticleParser(new Parser(), new ClassResourceWebClient());
        parser.parse(article);
        assertTrue(article.getContent().get(0).getContent().startsWith(ARTICLE_START_TEXT));
    }

}
