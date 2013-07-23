package com.cee.news.servlet.highlight;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import com.cee.news.client.error.ServiceException;
import com.cee.news.highlighter.ContentHighlighter;
import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebClientFactory;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

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
			contentHighlighter.highlightContent(response.getWriter(), webResponse, article, new Settings());
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