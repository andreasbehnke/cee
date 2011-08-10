package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_CONTENT;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.cee.news.store.StoreException;

public abstract class JcrStoreBase {
    
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
        return content;
    }
}
