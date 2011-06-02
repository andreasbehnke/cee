package com.cee.news.parser.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import com.cee.news.model.Article;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.impl.BoilerpipeArticleParser;

public class TestBoilerpipeArticleParser {

    @Test
    public void testParse() throws ParserException, IOException {
        Article article = new Article();
        article.setLocation(getClass().getResource("spiegelArticle.html").toExternalForm());
        ArticleParser parser = new BoilerpipeArticleParser(new WebClient() {
            public InputStream openStream(URL location) throws IOException {
                return location.openStream();
            }

            public WebResponse openWebResponse(URL location) {
                return null;
            }
        });
        parser.parse(article);
        assertTrue(article.getContent().get(0).getContent().startsWith("Russischer "));
    }

}
