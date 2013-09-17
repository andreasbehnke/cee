package com.cee.news.parser;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleParser.Settings;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;

public class ArticleReader {

	private ArticleParser articleParser;
	
	public ArticleReader() {}
	
	public ArticleReader(ArticleParser articleParser) {
		this.articleParser = articleParser;
	}
	
	public void setArticleParser(ArticleParser articleParser) {
	    this.articleParser = articleParser;
    }
	
	public Article readArticle(Reader reader, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	try {
    		return articleParser.parse(reader, article, settings);
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }
    
    public Article readArticle(WebClient webClient, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	URL location = new URL(article.getLocation());
    	Reader reader = webClient.openReader(location);
    	return readArticle(reader, article, settings);
    }
    
    public Article readArticle(WebClient webClient, Article article) throws MalformedURLException, IOException, ParserException {
    	return readArticle(webClient, article, new Settings());
    }
    
    public Article readArticle(WebResponse response, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	Reader reader = response.openReaderSource().getReader();
    	return readArticle(reader, article, settings);
    }
    
    public Article readArticle(WebResponse response, Article article) throws MalformedURLException, IOException, ParserException {
    	return readArticle(response, article, new Settings());
    }
}