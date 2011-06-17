package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.cee.news.store.StoreException;

public abstract class JcrStoreBase {
    
    private final static String SELECT_SITE_BY_NAME = "SELECT * FROM [news:site] WHERE [news:name]='%s'";
    
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

    protected Node getSiteNode(String name) throws RepositoryException {
        testSession();
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(String.format(SELECT_SITE_BY_NAME, name), Query.JCR_SQL2);
        NodeIterator iter = q.execute().getNodes();
        if (iter.hasNext()) {
            Node siteNode = iter.nextNode();
            if (iter.hasNext()) {
                throw new IllegalStateException("There must only one site with same URL location");
            }
            return siteNode;
        } else {
            return null;
        }
    }
}
