package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ArticleParser.Settings;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;

public class ArticleReader {

	private static final Logger LOG = LoggerFactory.getLogger(ArticleReader.class);	

	private ArticleParser articleParser;
	
	public ArticleReader() {}
	
	public ArticleReader(ArticleParser articleParser) {
		this.articleParser = articleParser;
	}
	
	public void setArticleParser(ArticleParser articleParser) {
	    this.articleParser = articleParser;
    }
	
	public Article readArticle(Reader reader, Article article, Settings settings) throws MalformedURLException, IOException {
    	try {
    		return articleParser.parse(reader, article, settings);
    	} catch(Exception ex) {
    		LOG.error("Could not parse article {}: {}", article.getLocation(), ex);
    		return null;
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }
    
    public Article readArticle(WebClient webClient, Article article, Settings settings) throws MalformedURLException, IOException {
    	URL location = new URL(article.getLocation());
    	Reader reader = webClient.openReader(location);
    	return readArticle(reader, article, settings);
    }
    
    public Article readArticle(WebClient webClient, Article article) throws MalformedURLException, IOException {
    	return readArticle(webClient, article, new Settings());
    }
    
    public Article readArticle(WebResponse response, Article article, Settings settings) throws MalformedURLException, IOException {
    	Reader reader = response.openReader();
    	return readArticle(reader, article, settings);
    }
    
    public Article readArticle(WebResponse response, Article article) throws MalformedURLException, IOException {
    	return readArticle(response, article, new Settings());
    }
}
