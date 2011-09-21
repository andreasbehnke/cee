package com.cee.news.store.jcr;

import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.cee.news.model.EntityKey;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;
import com.cee.news.store.StoreException;

public class JcrArticleSearchService extends JcrStoreBase implements ArticleSearchService {

	private static final String XPATH_SIMILAR_ARTICLES = "//element(*, news:article)[rep:similar(., '/news:content/%s')] order by @jcr:score descending";
	
	public JcrArticleSearchService() {
	}
	
	public JcrArticleSearchService(Session session) throws StoreException {
		setSession(session);
	}
	
	
	@Override
	public List<EntityKey> findArticles(List<String> sites, String fulltextSearchQuery) throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityKey> findRelatedArticles(List<String> sites, String articleKey) throws SearchException {
		testSession();
		if (sites == null) {
			throw new IllegalArgumentException("Parameter sites must not be null");
		}
		if (articleKey == null) {
			throw new IllegalArgumentException("Parameter articleKey must not be null");
		}
		try {
			QueryManager queryManager = getSession().getWorkspace().getQueryManager();
	        
			//TODO: only search within selected sites!
			Query q = queryManager.createQuery(String.format(XPATH_SIMILAR_ARTICLES, articleKey), Query.XPATH);
	        return buildPathList(q.execute().getNodes(), articleKey);
		} catch (RepositoryException e) {
			throw new SearchException("Could not perform similarity query", e);
		}
	}

}
