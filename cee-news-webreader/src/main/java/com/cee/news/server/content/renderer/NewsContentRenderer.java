package com.cee.news.server.content.renderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.cee.news.client.content.EntityContent;
import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.TextBlock;

public class NewsContentRenderer extends DefaultContentRenderer<ArticleKey, Article> {

    public static String DESCRIPTION_TEMPLATE = "Description Template";
    
    public static String CONTENT_TEMPLATE = "Content Template";
    
    // TODO localized date format
    protected String formatDate(Calendar calendar) {
        return SimpleDateFormat.getDateInstance().format(calendar.getTime());
    }
    
    protected EntityContent<ArticleKey> renderDescription(ArticleKey articleKey, Article article) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h3>").append(article.getTitle()).append("</h3>")
                .append("<p>").append(formatDate(article.getPublishedDate())).append("</p>");
        if (articleKey.getScore() != -1) {
            builder.append("<p>").append(articleKey.getScore()).append("</p>");
        }
        builder.append("<p>").append(article.getShortText()).append("</p>");
        return new EntityContent<ArticleKey>(articleKey, builder.toString());
    }

    protected EntityContent<ArticleKey> renderContent(ArticleKey articleKey, Article article) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>").append(article.getTitle()).append("</h1>")
            .append("<p>").append(formatDate(article.getPublishedDate())).append("</p>")
            .append("<p><a href=\"").append(article.getLocation()).append("\" target=\"article\">open article</a></p>");
        for (TextBlock textBlock : article.getContent()) {
            builder.append("<p>").append(textBlock.getContent()).append("</p>");
        }
        return new EntityContent<ArticleKey>(articleKey, builder.toString());
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
