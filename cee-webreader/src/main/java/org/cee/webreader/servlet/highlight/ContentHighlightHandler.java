package org.cee.webreader.servlet.highlight;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cee.highlighter.ContentHighlighter;
import org.cee.highlighter.DefaultSettings;
import org.cee.highlighter.ContentHighlighter.Settings;
import org.cee.news.model.Article;
import org.cee.news.model.ArticleKey;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.StoreException;
import org.cee.parser.ParserException;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;
import org.cee.parser.net.WebResponse;
import org.cee.webreader.client.error.ServiceException;
import org.springframework.web.HttpRequestHandler;

public class ContentHighlightHandler implements HttpRequestHandler {
	
	private ArticleStore articleStore;
	
	private WebClientFactory webClientFactory;
	
	private ContentHighlighter contentHighlighter;
	
	public void setArticleStore(ArticleStore articleStore) {
		this.articleStore = articleStore;
	}
	
	public void setWebClientFactory(WebClientFactory webClientFactory) {
	    this.webClientFactory = webClientFactory;
    }

	public void setContentHighlighter(ContentHighlighter contentHighlighter) {
		this.contentHighlighter = contentHighlighter;
	}

	@Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		    ArticleKey articleKey = getArticleKey(request);
			Article article = articleStore.getArticle(articleKey, false);
			
			WebClient webClient = webClientFactory.createWebClient();
			WebResponse webResponse = webClient.openWebResponse(new URL(article.getLocation()));
			String contentEncoding = webResponse.openReaderSource().getContentEncoding();
			response.setCharacterEncoding(contentEncoding);
			
			Settings settings = DefaultSettings.createDefaultSettings(new URL(article.getLocation()));
			contentHighlighter.highlightContent(response.getWriter(), webResponse, article, settings);
		} catch(StoreException e) {
			throw new ServletException(e);
		} catch (ParserException e) {
			throw new ServletException(e);
        }
	}
	
	private ArticleKey getArticleKey(HttpServletRequest request) {
		String url = request.getRequestURI();
		String[] pathFragments = url.split("/");
		int articleKeyIndex = pathFragments.length - 1;
		int siteKeyIndex = pathFragments.length - 2;
		String articleKey = decode(pathFragments[articleKeyIndex]);
		String siteKey = decode(pathFragments[siteKeyIndex]);
		return ArticleKey.get(null, articleKey, siteKey);
	}
	
	private String decode(String input) {
		try {
	        return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
	        throw new ServiceException(e.getLocalizedMessage());
        }
	}
}