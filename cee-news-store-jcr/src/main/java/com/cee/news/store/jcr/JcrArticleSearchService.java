package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.PATH_CONTENT;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_TITLE;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

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
	
	protected List<EntityKey> buildPathList(RowIterator iterator, List<String> sites, String comparedArticleKey) throws RepositoryException {
		List<EntityKey> keys = new ArrayList<EntityKey>();
		while (iterator.hasNext() && keys.size() < DEFAULT_QUERY_LIMIT) {
			Row row = iterator.nextRow();
			System.out.println(row.toString());
			double score = row.getScore();
			Node node = row.getNode();
			String articlePath = node.getPath().replace(PATH_CONTENT, "");
			if (!comparedArticleKey.equals(articlePath)) {
				String siteName = articlePath.substring(0, articlePath.indexOf('/'));
		    	if (sites.contains(siteName)) {
		    		String articleTitle = node.getProperty(PROP_TITLE).getString() + " : " + score;
		    		keys.add(new EntityKey(articleTitle, articlePath, score));
		    	}
			}
	    }
	    return keys;
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
			Query q = queryManager.createQuery(String.format(XPATH_SIMILAR_ARTICLES, articleKey), Query.XPATH);
	        return buildPathList(q.execute().getRows(), sites, articleKey);
		} catch (RepositoryException e) {
			throw new SearchException("Could not perform similarity query", e);
		}
	}

}
