package com.cee.news.search;

import java.util.List;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;

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
