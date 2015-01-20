package org.cee.webreader.server.content.renderer;

/*
 * #%L
 * News Reader
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


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.cee.store.article.Article;
import org.cee.store.article.ArticleKey;
import org.cee.store.article.TextBlock;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.error.ServiceException;

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
	        	.appendLink("/NewsReader/content/highlight/" + encodedSiteKey + "/" + encodedArticleKey, "article", "open article");
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
