package com.cee.news.server.content.renderer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.TextBlock;

public class NewsContentRenderer extends DefaultContentRenderer<ArticleKey, Article> {
    
    public static String DESCRIPTION_TEMPLATE = "Description Template";
    
    public static String CONTENT_TEMPLATE = "Content Template";

    protected EntityContent<ArticleKey> renderDescription(ArticleKey articleKey, Article article) {
        StringBuilder buffer = new StringBuilder();
        new HtmlBuilder(buffer)
        	.appendHtmlElement("h3", article.getTitle())
        	.appendIfNotNull("p", article.getPublishedDate())
        	.appendIfIsNumber("p", articleKey.getScore())
        	.appendIfNotNull("p", article.getShortText());
        return new EntityContent<ArticleKey>(articleKey, buffer.toString());
    }

    protected EntityContent<ArticleKey> renderContent(ArticleKey articleKey, Article article) {
        StringBuilder buffer = new StringBuilder();
        try {
        	String encodedSiteKey = URLEncoder.encode(articleKey.getSiteKey(), "UTF-8");
        	String encodedArticleKey = URLEncoder.encode(articleKey.getKey(), "UTF-8");
        	HtmlBuilder builder = new HtmlBuilder(buffer)
	        	.appendHtmlElement("h1", article.getTitle())
	        	.appendIfNotNull("p", article.getPublishedDate())
	        	.appendHtmlElement("p", articleKey.getSiteKey())
	        	.appendLink("http://127.0.0.1:8888/NewsReader/content/highlight/" + encodedSiteKey + "/" + encodedArticleKey, "article", "open article");
	        for (TextBlock textBlock : article.getContent()) {
	        	builder.appendHtmlElement("p", textBlock.getContent());
	        }
        } catch (UnsupportedEncodingException e) {
	        throw new ServiceException(e.getMessage());
        }
        return new EntityContent<ArticleKey>(articleKey, buffer.toString());
    }
    
    public EntityContent<ArticleKey> render(ArticleKey key, Article article, String templateName) {
        if (key == null) {
            throw new IllegalArgumentException("Parameter key must not be null");
        }
        if (article == null) {
            throw new IllegalArgumentException("Parameter article must not be null");
        }
        if (DESCRIPTION_TEMPLATE.equals(templateName)) {
            return renderDescription(key, article);
        } else {
            return renderContent(key, article);
        }
    };
}
