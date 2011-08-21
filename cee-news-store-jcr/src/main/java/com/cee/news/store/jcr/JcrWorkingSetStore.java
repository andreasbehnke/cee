/**
 * 
 */
package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.NODE_WORKINGSET;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_NAME;
import static com.cee.news.store.jcr.JcrStoreConstants.PROP_SITES;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.cee.news.model.NamedKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

/**
 * JCR implementation of the {@link WorkingSetStore}.
 */
public class JcrWorkingSetStore extends JcrStoreBase implements WorkingSetStore {
    
    private final static String SELECT_WORKING_SET_BY_NAME = "SELECT * FROM [news:workingSet] WHERE [news:name]='%s'";

    private final static String SELECT_WORKING_SETS_ORDERED_BY_NAME = "SELECT [news:name] FROM [news:workingSet] ORDER BY [news:name] ASC";
    
    public JcrWorkingSetStore() {
    }

    public JcrWorkingSetStore(Session session) throws StoreException {
        setSession(session);
    }
    
    protected Node getWorkingSetNode(String name) throws RepositoryException {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name must not be null");
        }
        testSession();
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

    public void update(WorkingSet workingSet) throws StoreException {
        try {
            Node workingSetNode = getWorkingSetNode(workingSet.getName());
            if (workingSetNode == null) {
                workingSetNode = getContent().addNode(NODE_WORKINGSET, NODE_WORKINGSET);
                workingSetNode.setProperty(PROP_NAME, workingSet.getName());
            }
            String[] sites = new String[workingSet.getSites().size()];
            List<String> siteList = NamedKeyUtil.extractKeys(workingSet.getSites());
            for (int i=0; i < siteList.size(); i++) {
                sites[i] = siteList.get(i);
            }
            workingSetNode.setProperty(PROP_SITES, sites);
            getSession().save();
        } catch (RepositoryException e) {
            throw new StoreException(workingSet, "Could not update working set", e);
        }
    }

    public void rename(String oldName, String newName) throws StoreException {
        try {
            Node workingSetNode = getWorkingSetNode(oldName);
            if (workingSetNode == null) {
                throw new IllegalArgumentException("The weorking set with name " + oldName + " does not exists!");
            }
            workingSetNode.setProperty(PROP_NAME, newName);
            getSession().save();
        } catch (RepositoryException e) {
            throw new StoreException(oldName, "Could not rename working set", e);
        }
    }
    
    @Override
    public boolean contains(String name) throws StoreException {
    	try {
			return getWorkingSetNode(name) != null;
		} catch (RepositoryException e) {
			throw new StoreException(name, "Could not test existence of working set", e);
		}
    }

    public WorkingSet getWorkingSet(String name) throws StoreException {
        try {
            Node wsNode = getWorkingSetNode(name);
            if (wsNode == null) {
                return null;
            }
            WorkingSet workingSet = new WorkingSet();
            workingSet.setName(wsNode.getProperty(PROP_NAME).getString());
            List<NamedKey> sites = new ArrayList<NamedKey>();
            for (Value value : wsNode.getProperty(PROP_SITES).getValues()) {
                sites.add(new NamedKey(value.getString(), value.getString()));
            }
            workingSet.setSites(sites);
            return workingSet;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve working set", e);
        }
    }

    protected NodeIterator getWorkingSetNodesOrderedByName() throws RepositoryException {
        testSession();
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(SELECT_WORKING_SETS_ORDERED_BY_NAME, Query.JCR_SQL2);
        return q.execute().getNodes();
    }
    
    public List<NamedKey> getWorkingSetsOrderedByName() throws StoreException {
        try {
            List<NamedKey> workingSets = new ArrayList<NamedKey>();
            NodeIterator iter = getWorkingSetNodesOrderedByName();
            while (iter.hasNext()) {
            	String name = iter.nextNode().getProperty(PROP_NAME).getString();
                workingSets.add(new NamedKey(name, name));
            }
            return workingSets;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve working set nodes", e);
        }
    }

    public long getWorkingSetCount() throws StoreException {
        try {
            return getWorkingSetNodesOrderedByName().getSize();
        } catch (RepositoryException e) {
            throw new StoreException(null, e);
        }
    }
}
