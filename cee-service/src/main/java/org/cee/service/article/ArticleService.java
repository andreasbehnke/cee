package org.cee.service.article;

import java.util.ArrayList;
import java.util.List;

import org.cee.search.ArticleSearchService;
import org.cee.search.SearchException;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.article.Article;
import org.cee.store.article.ArticleKey;
import org.cee.store.article.ArticleStore;
import org.cee.store.workingset.WorkingSet;
import org.cee.store.workingset.WorkingSetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);

	private static final String PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL = "Parameter articleKey must not be null";

    private static final String PARAMETER_SEARCH_QUERY_MUST_NOT_BE_NULL = "Parameter searchQuery must not be null";

    private static final String PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL = "Parameter workingSetKey must not be null";

    private static final String PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL = "Parameter siteKeys must not be null";

    private static final String PARAMETER_SITE_KEY_MUST_NOT_BE_NULL = "Parameter siteKey must not be null";

	private static final String RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE = "Retrieved {} related articles for article {}";

	private static final String WORKING_SET_NOT_FOUND = "Working set %s not found";

	private static final String RETRIEVED_ARTICLES_FOR_WORKING_SET = "Retrieved %s articles for working set %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITE = "Retrieved %s articles for site %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITES = "Retrieved {} articles for {} sites";
	    
    private ArticleStore articleStore;

	private ArticleSearchService articleSearchService;
	
	private WorkingSetStore workingSetStore;
	
	public void setArticleStore(ArticleStore articleStore) {
		this.articleStore = articleStore;
	}
	
	public void setArticleSearchService(ArticleSearchService articleSearchService) {
		this.articleSearchService = articleSearchService;
	}

	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}
	
	public List<ArticleKey> articlesOfSite(EntityKey siteKey) throws StoreException {
	    if (siteKey == null) {
	        throw new IllegalArgumentException(PARAMETER_SITE_KEY_MUST_NOT_BE_NULL);
	    }
		List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(siteKey);
		LOG.debug(RETRIEVED_ARTICLES_FOR_SITE, keys.size(), siteKey);
		return keys;
	}
	
	public List<ArticleKey> articlesOfSites(List<EntityKey> siteKeys) throws StoreException {
	    if (siteKeys == null) {
            throw new IllegalArgumentException(PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL);
        }
	    List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(siteKeys);
        LOG.debug(RETRIEVED_ARTICLES_FOR_SITES, keys.size(), siteKeys.size());
        return keys;
	}
	
	public List<ArticleKey> articlesOfWorkingSet(EntityKey workingSetKey) throws StoreException {
	    if (workingSetKey == null) {
            throw new IllegalArgumentException(PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL);
        }
		WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetKey);
		List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(workingSet);
		LOG.debug(RETRIEVED_ARTICLES_FOR_WORKING_SET, keys.size(), workingSetKey);
		return keys;
	}
	
	public List<ArticleKey> relatedArticles(ArticleKey articleKey, EntityKey workingSetKey) throws StoreException, SearchException {
	    if (articleKey == null) {
            throw new IllegalArgumentException(PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL);
        }
	    if (workingSetKey == null) {
            throw new IllegalArgumentException(PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL);
        }
        WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
		if (ws == null) {
			throw new IllegalArgumentException(String.format(WORKING_SET_NOT_FOUND, workingSetKey));
		}
		List<EntityKey> sites = ws.getSites();
		List<EntityKey> relatedSites = null;
		
		if (sites.size() == 1) {
			relatedSites = sites;
		} else {
			//remove site of current article
			relatedSites = new ArrayList<EntityKey>(sites);
			String siteOfArticle = articleKey.getSiteKey();
			relatedSites.remove(EntityKey.get(siteOfArticle));
		}
		List<ArticleKey> keys = articleSearchService.findRelatedArticles(relatedSites, articleKey, ws.getLanguage());
		LOG.debug(RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE, keys.size(), articleKey);
		return keys;
	}
	
	public List<ArticleKey> findArticles(List<EntityKey> siteKeys, EntityKey workingSetKey, String searchQuery) throws StoreException, SearchException {
	    if (siteKeys == null) {
	        throw new IllegalArgumentException(PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL);
	    }
	    if (searchQuery == null) {
	        throw new IllegalArgumentException(PARAMETER_SEARCH_QUERY_MUST_NOT_BE_NULL);
	    }
	    WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
	    return articleSearchService.findArticles(siteKeys, searchQuery, ws.getLanguage());
	}
	
	public Article get(ArticleKey articleKey) throws StoreException {
		return articleStore.getArticle(articleKey, true);
	}
	
	public List<Article> get(List<ArticleKey> articleKeys) throws StoreException {
		return articleStore.getArticles(articleKeys, true);
	}
}