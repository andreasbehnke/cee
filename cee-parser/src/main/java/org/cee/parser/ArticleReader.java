package org.cee.parser;

/*
 * #%L
 * Content Extraction Engine - News Parser
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
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.cee.parser.ArticleParser.Settings;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebResponse;
import org.cee.store.article.Article;

public class ArticleReader {

	private ArticleParser articleParser;
	
	public ArticleReader() {}
	
	public ArticleReader(ArticleParser articleParser) {
		this.articleParser = articleParser;
	}
	
	public void setArticleParser(ArticleParser articleParser) {
	    this.articleParser = articleParser;
    }
	
	private Article readArticle(Reader reader, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	try {
    		return articleParser.parse(reader, article, settings);
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }
    
    public Article readArticle(WebClient webClient, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	URL location = new URL(article.getLocation());
    	Reader reader = webClient.openWebResponse(location).openReaderSource().getReader();
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
