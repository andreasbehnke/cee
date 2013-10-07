package org.cee.store.jcr;

import static org.cee.store.jcr.JcrStoreConstants.NODE_CONTENT;
import static org.cee.store.jcr.JcrStoreConstants.SLASH;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.util.Text;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;

public abstract class JcrStoreBase {
    
	protected static final String SITE_NAME_SELECTOR = "siteName";

	protected static final String ARTICLE_ID_SELECTOR = "articleId";

	protected static final String ARTICLE_TITLE_SELECTOR = "articleTitle";

	protected static final String OR = "OR ";

	protected static final String WHERE_SITE_NAME_TERM = "s.[news:name] = '%s' ";

	protected static final String WHERE_ARTICLE_PATH_TERM = "PATH(a) = '%s' ";
	
	protected static final int DEFAULT_QUERY_LIMIT = 200;
	
	private SessionManager sessionManager;
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	protected Session getSession() {
        return sessionManager.getSession();
    }
    
    protected Node getContent() throws PathNotFoundException, RepositoryException {
    	return getSession().getRootNode().getNode(NODE_CONTENT);
    }
    
    protected String getStringPropertyOrNull(Node node, String relPath) throws RepositoryException {
    	if (node.hasProperty(relPath)) {
            return node.getProperty(relPath).getString();
        } else {
        	return null;
        }
    }
    
    protected Node getNodeOrNull(Node parent, String relPath) throws RepositoryException {
    	if (parent.hasNode(relPath)) {
        	return parent.getNode(relPath);
        } else {
        	return null;
        }
    }
    
    protected Node getContentNodeOrNull(String relPath) throws RepositoryException {
    	return getNodeOrNull(getContent(), relPath);
    }
    
    protected boolean containsContentNode(String relPath) throws RepositoryException {
    	return getContent().hasNode(relPath);
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
