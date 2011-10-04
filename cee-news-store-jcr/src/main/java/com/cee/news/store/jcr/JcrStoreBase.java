package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.EntityKey;
import com.cee.news.store.StoreException;

public abstract class JcrStoreBase {
    
	private static final Logger LOG = LoggerFactory.getLogger(JcrStoreBase.class);

	protected static final String SITE_NAME_SELECTOR = "siteName";

	protected static final String ARTICLE_ID_SELECTOR = "articleId";

	protected static final String ARTICLE_TITLE_SELECTOR = "articleTitle";

	protected static final String OR = "OR ";

	protected static final String WHERE_SITE_NAME_TERM = "s.[news:name] = '%s' ";

	protected static final int DEFAULT_QUERY_LIMIT = 200;
	
    protected static String getArticlePath(String articleId) {
		return Text.escapeIllegalJcrChars(articleId);
	}

	protected static String getArticlePath(String siteName, String articleId) {
		return new StringBuilder(300)
			.append(JcrSiteStore.getSitePath(siteName))
			.append(SLASH)
			.append(getArticlePath(articleId))
			.toString();
	}

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
    
    protected Session getSession() {
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
    
    public void dumpContent() throws RepositoryException {
    	dumpNode(getContent());
    }
    
    /** Recursively outputs the contents of the given node. */
    public void dumpNode(Node node) throws RepositoryException {
        // First output the node path
        System.out.println(node.getPath());
        // Skip the virtual (and large!) jcr:system subtree
        /*if (node.getName().equals("jcr:system")) {
            return;
        }*/

        // Then output the properties
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                // A multi-valued property, print all values
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    System.out.println(
                        property.getPath() + " = " + values[i].getString());
                }
            } else {
                // A single-valued property
                System.out.println(
                    property.getPath() + " = " + property.getString());
            }
        }

        // Finally output all the child nodes recursively
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dumpNode(nodes.nextNode());
        }
    }

	protected List<EntityKey> buildPathList(RowIterator iterator) throws RepositoryException {
		List<EntityKey> pathes = new ArrayList<EntityKey>();
		while (iterator.hasNext()) {
	    	Row row = iterator.nextRow();
	    	String articleId = row.getValue(ARTICLE_ID_SELECTOR).getString();
	    	String articleTitle = row.getValue(ARTICLE_TITLE_SELECTOR).getString();
	    	String siteName = row.getValue(SITE_NAME_SELECTOR).getString();
	        pathes.add(new EntityKey(articleTitle, getArticlePath(siteName, articleId)));
	    }
	    return pathes;
	}

	protected String buildExpression(String formatStr, String operator, List<String> keys) {
		boolean isFirst = true;
		StringBuilder buffer = new StringBuilder();
		for (String key : keys) {
			if (isFirst) {
				isFirst = false;
			} else {
				buffer.append(operator);
			}
			buffer.append(String.format(formatStr, key));
		}
		return buffer.toString();
	}
}
