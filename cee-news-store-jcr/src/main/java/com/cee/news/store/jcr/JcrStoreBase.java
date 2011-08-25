package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_CONTENT;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.store.StoreException;

public abstract class JcrStoreBase {
    
	private static final Logger LOG = LoggerFactory.getLogger(JcrStoreBase.class);
	
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
}
