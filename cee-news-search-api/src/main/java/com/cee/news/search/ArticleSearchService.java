package com.cee.news.search;

import java.util.List;

import com.cee.news.model.EntityKey;

/**
 * The search service provides methods for fulltext search and for finding similar articles
 * @author andreasbehnke
 */
public interface ArticleSearchService {

	/**
	 * Searches the fulltext index
	 * @param sites The sites to search within
	 * @param fulltextSearchQuery The fulltext query to be used
	 * @return List of hits
	 */
	List<EntityKey> findArticles(List<String> sites, String fulltextSearchQuery) throws SearchException;
	
	/**
	 * Searches the index for matching (related) articles
	 * @param sites The sites to search within
	 * @param articleKey The article key to be compared to
	 * @return List of related articles
	 */
	List<EntityKey> findRelatedArticles(List<String> sites, String articleKey) throws SearchException;
}
