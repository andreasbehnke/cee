package com.cee.news.store.jcr;

import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;

public class JcrArticleSearchService extends JcrStoreBase implements ArticleSearchService {

	private static final String XPATH_SIMILAR_ARTICLES = "//element(*, news:article)[rep:similar(., '/news:content/%s')]";
	
	@Override
	public List<EntityKey> findArticles(WorkingSet context, String fulltextSearchQuery) throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityKey> findRelatedArticles(WorkingSet context, String articleKey) throws SearchException {
		testSession();
		if (context == null) {
			throw new IllegalArgumentException("Parameter context must not be null");
		}
		if (articleKey == null) {
			throw new IllegalArgumentException("Parameter articleKey must not be null");
		}
		try {
			QueryManager queryManager = getSession().getWorkspace().getQueryManager();
	        Query q = queryManager.createQuery(String.format(XPATH_SIMILAR_ARTICLES, articleKey), Query.XPATH);
	        return buildPathList(q.execute().getNodes());
		} catch (RepositoryException e) {
			throw new SearchException("Could not perform similarity query", e);
		}
	}

}
