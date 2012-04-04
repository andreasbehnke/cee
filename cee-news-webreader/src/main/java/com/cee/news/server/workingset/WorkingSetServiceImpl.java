package com.cee.news.server.workingset;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.async.EntityUpdateResult.State;
import com.cee.news.client.error.ServiceException;
import com.cee.news.client.workingset.WorkingSetData;
import com.cee.news.client.workingset.WorkingSetService;
import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class WorkingSetServiceImpl implements WorkingSetService {
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkingSetServiceImpl.class);

    private static final String COULD_NOT_UPDATE_WORKING_SET = "Could not update working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET = "Could not retrieve working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET_LIST = "Could not retrieve working set list";

	private WorkingSetStore workingSetStore;

    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    @Override
    public List<EntityKey> getWorkingSetsOrderedByName() {
        try {
            return workingSetStore.getWorkingSetsOrderedByName();
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET_LIST);
        }
    }

    @Override
    public WorkingSetData getWorkingSet(EntityKey workingSetKey) {
        try {
            WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetKey);
            WorkingSetData wsd = new WorkingSetData();
            wsd.setIsNew(false);
            wsd.setNewName(workingSet.getName());
            wsd.setOldName(workingSet.getName());
            wsd.setSites(workingSet.getSites());
            return wsd;
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET);
        }
    }
    
    @Override
    public EntityUpdateResult update(WorkingSetData wsd) {
        try {
            String newName = wsd.getNewName();
            String oldName = wsd.getOldName();
            if (wsd.getIsNew() && workingSetStore.getWorkingSet(EntityKey.get(newName)) != null) {
                return new EntityUpdateResult(State.entityExists, null);
            }
            if (!wsd.getIsNew() && !newName.equals(oldName)) {
                workingSetStore.rename(oldName, newName);
            }
            WorkingSet workingSet = null;
            if (wsd.getIsNew()) {
            	workingSet = new WorkingSet();
            	workingSet.setName(newName);
            } else {
            	workingSet = workingSetStore.getWorkingSet(EntityKey.get(newName));
            }
            workingSet.setSites(wsd.getSites());
            EntityKey key = workingSetStore.update(workingSet);
            return new EntityUpdateResult(State.ok, key);
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_UPDATE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_UPDATE_WORKING_SET);
        }
    }
    
    @Override
    public WorkingSetData addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey) {
        WorkingSetData workingSet = getWorkingSet(workingSetKey);
        workingSet.getSites().add(siteKey);
        update(workingSet);
        return workingSet;
    }
}
