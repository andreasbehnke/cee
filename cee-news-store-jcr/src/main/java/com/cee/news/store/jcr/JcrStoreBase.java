package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_CONTENT;
import static com.cee.news.store.jcr.JcrStoreConstants.SLASH;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.store.StoreException;

public abstract class JcrStoreBase {
    
	private static final Logger LOG = LoggerFactory.getLogger(JcrStoreBase.class);

	protected static final String SITE_NAME_SELECTOR = "siteName";

	protected static final String ARTICLE_ID_SELECTOR = "articleId";

	protected static final String ARTICLE_TITLE_SELECTOR = "articleTitle";

	protected static final String OR = "OR ";

	protected static final String WHERE_SITE_NAME_TERM = "s.[news:name] = '%s' ";

	protected static final String WHERE_ARTICLE_PATH_TERM = "PATH(a) = '%s' ";
	
	protected static final int DEFAULT_QUERY_LIMIT = 200;
	
	private Session session;

    private Node content;

    public void setSession(Session session) throws StoreException {
        if (session == null) {
            throw new IllegalArgumentException("Parameter session must not be null");
        }
        this.session = session;
        try {
            Node root = session.getRootNode();
            this.content = root.getNode(NODE_CONTENT);
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Session initialized for instance {}: {}", this, session);
            }
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve root node from repository", e);
        }
    }
    
    public Session getSession() {
        return session;
    }

    protected void testSession() {
        if (session == null) {
            throw new IllegalStateException("No session has been initialized");
        }
    }
    
    protected Node getContent() {
    	testSession();
        return content;
    }
    
    protected Node getNodeOrNull(Node parent, String relPath) throws RepositoryException {
    	testSession();
    	if (parent.hasNode(relPath)) {
        	return parent.getNode(relPath);
        } else {
        	return null;
        }
    }
    
    protected Node getContentNodeOrNull(String relPath) throws RepositoryException {
    	return getNodeOrNull(getContent(), relPath);
    }

	protected static String buildArticlePath(ArticleKey articleKey) {
		return new StringBuilder(300)
			.append(Text.escapeIllegalJcrChars(articleKey.getSiteKey()))
			.append(SLASH)
			.append(Text.escapeIllegalJcrChars(articleKey.getKey()))
			.toString();
	}

	protected List<ArticleKey> buildArticleList(RowIterator iterator) throws RepositoryException {
		List<ArticleKey> articles = new ArrayList<ArticleKey>();
		while (iterator.hasNext()) {
	    	Row row = iterator.nextRow();
	    	String articleId = row.getValue(ARTICLE_ID_SELECTOR).getString();
	    	String articleTitle = row.getValue(ARTICLE_TITLE_SELECTOR).getString();
	    	String siteName = row.getValue(SITE_NAME_SELECTOR).getString();
	        articles.add(ArticleKey.get(articleTitle, articleId, siteName));
	    }
	    return articles;
	}

	protected String buildExpression(String formatStr, String operator, List<EntityKey> keys) {
		boolean isFirst = true;
		StringBuilder buffer = new StringBuilder();
		for (EntityKey key : keys) {
			if (isFirst) {
				isFirst = false;
			} else {
				buffer.append(operator);
			}
			buffer.append(String.format(formatStr, key.getKey()));
		}
		return buffer.toString();
	}
}
