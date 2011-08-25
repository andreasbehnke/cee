package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_FEED;
import static com.cee.news.store.jcr.JcrStoreConstants.NODE_SITE;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_ACTIVE;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_CONTENT_TYPE;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_DESCRIPTION;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_LOCATION;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_NAME;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_TITLE;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.jackrabbit.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Feed;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;

/**
 * JCR implementation of the {@link SiteStore}
 */
public class JcrSiteStore extends JcrStoreBase implements SiteStore {
	
	private final static Logger LOG = LoggerFactory.getLogger(JcrSiteStore.class);

	private final static String SELECT_SITES_ORDERED_BY_NAME = "SELECT [news:name] FROM [news:site] ORDER BY [news:name]";

    public JcrSiteStore() {
    }

    public JcrSiteStore(Session session) throws StoreException {
        setSession(session);
    }
    
    protected static String getSitePath(String name) {
    	return Text.escapeIllegalJcrChars(name);
    }
    
    protected boolean hasSiteNode(String name) throws RepositoryException {
    	return getContent().hasNode(getSitePath(name));
    }
    
    protected Node getSiteNode(String name) throws RepositoryException {
        if (name == null) {
        	throw new IllegalArgumentException("Parameter name must not be null");
        }
    	return getContentNodeOrNull(getSitePath(name));
    }
    
    protected Node createSiteNode(String name) throws RepositoryException {
        return getContent().addNode(getSitePath(name), NODE_SITE);
    }

    protected void addFeed(Node siteNode, Feed feed) throws RepositoryException {
        Node feedNode = siteNode.addNode(NODE_FEED, NODE_FEED);
        feedNode.setProperty(PROP_LOCATION, feed.getLocation());
        feedNode.setProperty(PROP_CONTENT_TYPE, feed.getContentType());
        feedNode.setProperty(PROP_TITLE, feed.getTitle());
        feedNode.setProperty(PROP_ACTIVE, feed.isActive());
    }

    public EntityKey update(Site site) throws StoreException {
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        Node siteNode = null;
        String name = site.getName();
        try {
            siteNode = getSiteNode(name);
        } catch (RepositoryException e) {
            throw new StoreException(site, "Could not retrieve site node from repository", e);
        }
        if (siteNode == null) {
            try {
                siteNode = createSiteNode(name);
                LOG.debug("Added site node for ", name);
            } catch (RepositoryException e) {
                throw new StoreException(site, "Could not create site node", e);
            }
        }
        try {
        	siteNode.setProperty(PROP_NAME, name);
            siteNode.setProperty(PROP_LOCATION, site.getLocation());
            siteNode.setProperty(PROP_TITLE, site.getTitle());
            siteNode.setProperty(PROP_DESCRIPTION, site.getDescription());
            NodeIterator feeds = siteNode.getNodes(NODE_FEED);
            while (feeds.hasNext()) {
                feeds.nextNode().remove();
            }
            for (Feed feed : site.getFeeds()) {
                addFeed(siteNode, feed);
            }
        } catch (RepositoryException e) {
            throw new StoreException(site, "Could not update site node", e);
        }
        try {
            getSession().save();
            LOG.debug("Stored site node for ", name);
            return new EntityKey(name, getSitePath(name));
        } catch (Exception e) {
            throw new StoreException(site, "Could not save session", e);
        }
    }
    
    @Override
    public boolean contains(String name) throws StoreException {
    	try {
            return hasSiteNode(name);
        } catch (RepositoryException e) {
            throw new StoreException(name, "Could not test existence of site", e);
        }
    }

    public Site getSite(String key) throws StoreException {
        if (key == null) {
            throw new IllegalArgumentException("Parameter key must not be null");
        }
        Node siteNode = null;
        try {
            siteNode = getContentNodeOrNull(key);
        } catch (RepositoryException e) {
            throw new StoreException("Could not query site node", e);
        }
        if (siteNode == null) {
        	LOG.warn("No site node found for {}", key);
            return null;
        }
        Site site = new Site();
        try {
        	site.setName(siteNode.getProperty(PROP_NAME).getString());
            site.setLocation(siteNode.getProperty(PROP_LOCATION).getString());
            if (siteNode.hasProperty(PROP_DESCRIPTION)) {
                site.setDescription(siteNode.getProperty(PROP_DESCRIPTION).getString());
            }
            if (siteNode.hasProperty(PROP_TITLE)) {
                site.setTitle(siteNode.getProperty(PROP_TITLE).getString());
            }
        } catch (RepositoryException e) {
            throw new StoreException("Could not populate site with properties", e);
        }
        try {
            NodeIterator iter = siteNode.getNodes(NODE_FEED);
            while (iter.hasNext()) {
                Node feedNode = iter.nextNode();
                Feed feed = new Feed(feedNode.getProperty(PROP_LOCATION).getString(), feedNode.getProperty(PROP_TITLE).getString(), feedNode.getProperty(PROP_CONTENT_TYPE).getString());
                feed.setActive(feedNode.getProperty(PROP_ACTIVE).getBoolean());
                site.getFeeds().add(feed);
            }
        } catch (RepositoryException e) {
            throw new StoreException("Could not fetch feeds of site", e);
        }
        LOG.debug("Found site node for {}", key);
        return site;
    }

    protected NodeIterator getSiteNodesOrderedByName() throws RepositoryException {
        testSession();
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(SELECT_SITES_ORDERED_BY_NAME, Query.JCR_SQL2);
        return q.execute().getNodes();
    }

    public List<EntityKey> getSitesOrderedByName() throws StoreException {
        try {
            List<EntityKey> sites = new ArrayList<EntityKey>();
            NodeIterator iter = getSiteNodesOrderedByName();
            while (iter.hasNext()) {
            	String name = iter.nextNode().getProperty(PROP_NAME).getString();
            	String path = getSitePath(name);
                sites.add(new EntityKey(name, path));
            }
            if(LOG.isDebugEnabled()) {
            	LOG.debug("Found {} sites in repository", sites.size());
            }
            return sites;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve site nodes", e);
        }
    }
}
