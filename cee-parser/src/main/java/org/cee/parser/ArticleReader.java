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

import org.cee.net.WebClient;
import org.cee.net.WebResponse;
import org.cee.parser.ArticleParser.Settings;
import org.cee.store.article.Article;

public class ArticleReader {

	private ArticleParser articleParser;
	
	private WebClient webClient;
	
	public ArticleReader() {}
	
	public ArticleReader(ArticleParser articleParser, WebClient webClient) {
		this.articleParser = articleParser;
		this.webClient = webClient;
	}
	
	public void setArticleParser(ArticleParser articleParser) {
	    this.articleParser = articleParser;
    }
	
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
	public Article readArticle(Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	URL location = new URL(article.getLocation());
    	try (Reader reader = webClient.openWebResponse(location, false).openReader()) {
    	    return articleParser.parse(reader, article, settings);
    	}
    }
    
    public Article readArticle(Article article) throws MalformedURLException, IOException, ParserException {
    	return readArticle(article, new Settings());
    }
    
    public Article readArticle(WebResponse response, Article article, Settings settings) throws MalformedURLException, IOException, ParserException {
    	try (Reader reader = response.openReader()) {
            return articleParser.parse(reader, article, settings);
        }
    }
    
    public Article readArticle(WebResponse response, Article article) throws MalformedURLException, IOException, ParserException {
    	return readArticle(response, article, new Settings());
    }
}
