package org.cee.search;

/*
 * #%L
 * Content Extraction Engine - News Search API
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

import java.util.List;

import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;

/**
 * The search service provides methods for fulltext search and for finding similar articles
 * @author andreasbehnke
 */
public interface ArticleSearchService {
	
	/**
	 * @return List of supported languages as two-letter code as defined by ISO-639 or null, if this
	 * search implementation is language independent.
	 */
	List<String> getSupportedLanguages();

	/**
	 * Searches the fulltext index
	 * @param sites The sites to search within
	 * @param fulltextSearchQuery The fulltext query to be used
	 * @return List of hits
	 */
	List<ArticleKey> findArticles(List<EntityKey> sites, String fulltextSearchQuery, String language) throws SearchException;
	
	/**
	 * Searches the index for matching (related) articles
	 * @param sites The sites to search within
	 * @param articleKey The article key to be compared to
	 * @return List of related articles
	 */
	List<ArticleKey> findRelatedArticles(List<EntityKey> sites, ArticleKey articleKey, String language) throws SearchException;
}
