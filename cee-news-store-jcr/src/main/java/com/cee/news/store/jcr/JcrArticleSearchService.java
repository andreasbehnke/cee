package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.PATH_CONTENT;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_ID;
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

import org.apache.jackrabbit.util.Text;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;
import com.cee.news.store.StoreException;

public class JcrArticleSearchService extends JcrStoreBase implements ArticleSearchService {

    private static final String SQL2_SEARCH_ARTICLES = "SELECT [news:article].* from [news:article] WHERE CONTAINS([news:article].*,'%s')";
    
	private static final String XPATH_SIMILAR_ARTICLES = "//element(*, news:article)[rep:similar(., '/news:content/%s')] order by @jcr:score descending";
	
	public JcrArticleSearchService() {
	}
	
	public JcrArticleSearchService(Session session) throws StoreException {
		setSession(session);
	}
	
	private boolean shouldArticleAdded(ArticleKey originalKey, String articleKey, String siteKey) {
	    if (originalKey == null) {
	        return true;
	    }
	    return !articleKey.equals(originalKey.getKey()) || !siteKey.equals(originalKey.getSiteKey());
	}
	
	protected List<ArticleKey> buildArticleList(RowIterator iterator, List<EntityKey> sites, ArticleKey comparedArticleKey) throws RepositoryException {
		List<ArticleKey> keys = new ArrayList<ArticleKey>();
		while (iterator.hasNext() && keys.size() < DEFAULT_QUERY_LIMIT) {
			Row row = iterator.nextRow();
			double score = row.getScore();
			Node node = row.getNode();
			String articlePath = node.getPath().replace(PATH_CONTENT, "");
			String siteName = Text.unescapeIllegalJcrChars(articlePath.substring(0, articlePath.indexOf('/')));
			String articleKey = node.getProperty(PROP_ID).getString();
			if (shouldArticleAdded(comparedArticleKey, articleKey, siteName)) {
			    if (sites.contains(EntityKey.get(siteName))) {
			        String articleTitle = node.getProperty(PROP_TITLE).getString() + " : " + score;
    		    	keys.add(ArticleKey.get(articleTitle, articleKey, siteName, score));
    		    }
			}
	    }
	    return keys;
	}
	
	@Override
	public List<ArticleKey> findArticles(List<EntityKey> sites, String fulltextSearchQuery) throws SearchException {
	    testSession();
        if (sites == null) {
            throw new IllegalArgumentException("Parameter sites must not be null");
        }
        if (fulltextSearchQuery == null) {
            throw new IllegalArgumentException("Parameter fulltextSearchQuery must not be null");
        }
        try {
            QueryManager queryManager = getSession().getWorkspace().getQueryManager();
            Query q = queryManager.createQuery(String.format(SQL2_SEARCH_ARTICLES, "%" + fulltextSearchQuery + "%"), Query.JCR_SQL2);
            return buildArticleList(q.execute().getRows(), sites, null);
        } catch (RepositoryException e) {
            throw new SearchException("Could not perform search query", e);
        }
	}

    @Override
	public List<ArticleKey> findRelatedArticles(List<EntityKey> sites, ArticleKey articleKey) throws SearchException {
		testSession();
		if (sites == null) {
			throw new IllegalArgumentException("Parameter sites must not be null");
		}
		if (articleKey == null) {
			throw new IllegalArgumentException("Parameter articleKey must not be null");
		}
		try {
			QueryManager queryManager = getSession().getWorkspace().getQueryManager();
			Query q = queryManager.createQuery(String.format(XPATH_SIMILAR_ARTICLES, buildArticlePath(articleKey)), Query.XPATH);
	        return buildArticleList(q.execute().getRows(), sites, articleKey);
		} catch (RepositoryException e) {
			throw new SearchException("Could not perform similarity query", e);
		}
	}

}
