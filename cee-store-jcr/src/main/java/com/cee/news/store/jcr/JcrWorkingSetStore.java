/**
 * 
 */
package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_WORKINGSET;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_LANGUAGE;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_NAME;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_SITES;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.cee.news.model.EntityKey;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.StoreException;
import org.cee.news.store.WorkingSetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JCR implementation of the {@link WorkingSetStore}.
 */
public class JcrWorkingSetStore extends JcrStoreBase implements WorkingSetStore {
	
	private final static Logger LOG = LoggerFactory.getLogger(JcrWorkingSetStore.class);
    
    private final static String SELECT_WORKING_SET_BY_NAME = "SELECT * FROM [news:workingSet] WHERE [news:name]='%s'";

    private final static String SELECT_WORKING_SETS_ORDERED_BY_NAME = "SELECT [news:name] FROM [news:workingSet] ORDER BY [news:name] ASC";
    
    public JcrWorkingSetStore() {
    }

    public JcrWorkingSetStore(SessionManager sessionManager) {
		setSessionManager(sessionManager);
	}
    
    protected Node getWorkingSetNodeByName(String name) throws RepositoryException {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name must not be null");
        }
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(String.format(SELECT_WORKING_SET_BY_NAME, name), Query.JCR_SQL2);
        NodeIterator iter = q.execute().getNodes();
        if (iter.hasNext()) {
            Node workingSetNode = iter.nextNode();
            if (iter.hasNext()) {
                throw new IllegalStateException("There must only one working set with same name");
            }
            return workingSetNode;
        } else {
            return null;
        }
    }

    @Override
    public EntityKey update(WorkingSet workingSet) throws StoreException {
        try {
        	String name = workingSet.getName();
        	EntityKey workingSetKey = EntityKey.get(name);
            Node workingSetNode = getWorkingSetNodeByName(name);
            if (workingSetNode == null) {
                workingSetNode = getContent().addNode(NODE_WORKINGSET, NODE_WORKINGSET);
                workingSetNode.setProperty(PROP_NAME, name);
                workingSetNode.setProperty(PROP_LANGUAGE, workingSet.getLanguage());
                if(LOG.isDebugEnabled()) {
                	LOG.debug("Added working set node for {}", name);
                }
            }
            String[] sites = new String[workingSet.getSites().size()];
            List<EntityKey> siteList = workingSet.getSites();
            for (int i=0; i < siteList.size(); i++) {
                sites[i] = siteList.get(i).getKey();
            }
            workingSetNode.setProperty(PROP_SITES, sites);
            getSession().save();
            LOG.debug("Stored working set {}", name);
            return workingSetKey;
        } catch (RepositoryException e) {
            throw new StoreException(workingSet, "Could not update working set", e);
        }
    }

    @Override
    public void rename(String oldName, String newName) throws StoreException {
        try {
            Node workingSetNode = getWorkingSetNodeByName(oldName);
            if (workingSetNode == null) {
                throw new IllegalArgumentException("The weorking set with name " + oldName + " does not exists!");
            }
            workingSetNode.setProperty(PROP_NAME, newName);
            LOG.debug("Renamed working set {} to {}", oldName, newName);
            getSession().save();
        } catch (RepositoryException e) {
            throw new StoreException(oldName, "Could not rename working set", e);
        }
    }
    
	@Override
	public void delete(EntityKey key) throws StoreException {
		if (key == null) {
    		throw new IllegalArgumentException("Parameter key must not be null");
    	}
		try {
            Node workingSetNode = getWorkingSetNodeByName(key.getName());
            if (workingSetNode == null) {
                throw new IllegalArgumentException("The working set with name " + key.getName() + " does not exists!");
            }
            workingSetNode.remove();
            LOG.debug("Removed working set {}", key.getName());
            getSession().save();
        } catch (RepositoryException e) {
            throw new StoreException(key.getName(), "Could not remove working set", e);
        }
	}
    
    @Override
    public boolean contains(String name) throws StoreException {
    	try {
			return getWorkingSetNodeByName(name) != null;
		} catch (RepositoryException e) {
			throw new StoreException(name, "Could not test existence of working set", e);
		}
    }

    @Override
    public WorkingSet getWorkingSet(EntityKey key) throws StoreException {
    	if (key == null) {
    		throw new IllegalArgumentException("Parameter key must not be null");
    	}
        try {
            Node wsNode = getWorkingSetNodeByName(key.getName());
            if (wsNode == null) {
            	LOG.warn("No working set node found for {}", key);
                return null;
            }
            WorkingSet workingSet = new WorkingSet();
            workingSet.setName(wsNode.getProperty(PROP_NAME).getString());
            workingSet.setLanguage(getStringPropertyOrNull(wsNode, PROP_LANGUAGE));
            List<EntityKey> sites = new ArrayList<EntityKey>();
            for (Value value : wsNode.getProperty(PROP_SITES).getValues()) {
                sites.add(EntityKey.get(value.getString()));
            }
            workingSet.setSites(sites);
            LOG.debug("Found working set {}", key);
            return workingSet;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve working set", e);
        }
    }

    protected NodeIterator getWorkingSetNodesOrderedByName() throws RepositoryException {
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(SELECT_WORKING_SETS_ORDERED_BY_NAME, Query.JCR_SQL2);
        return q.execute().getNodes();
    }
    
    @Override
    public List<EntityKey> getWorkingSetsOrderedByName() throws StoreException {
        try {
            List<EntityKey> workingSets = new ArrayList<EntityKey>();
            NodeIterator iter = getWorkingSetNodesOrderedByName();
            while (iter.hasNext()) {
            	String name = iter.nextNode().getProperty(PROP_NAME).getString();
                workingSets.add(EntityKey.get(name));
            }
            if(LOG.isDebugEnabled()) {
            	LOG.debug("Found {} working sets", workingSets.size());
            }
            return workingSets;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve working set nodes", e);
        }
    }

    @Override
    public long getWorkingSetCount() throws StoreException {
        try {
            return getWorkingSetNodesOrderedByName().getSize();
        } catch (RepositoryException e) {
            throw new StoreException(null, e);
        }
    }
}
