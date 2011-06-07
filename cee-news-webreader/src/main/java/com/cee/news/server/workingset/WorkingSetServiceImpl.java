package com.cee.news.server.workingset;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.workingset.WorkingSetData;
import com.cee.news.client.workingset.WorkingSetService;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class WorkingSetServiceImpl implements WorkingSetService {
	
	private static final String COULD_NOT_UPDATE_WORKING_SET = "Could not update working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET = "Could not retrieve working set";

	private static final String COULD_NOT_ASK_WORKING_SET = "Could not ask working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET_LIST = "Could not retrieve working set list";

	private static Logger log = LoggerFactory.getLogger(WorkingSetServiceImpl.class);

    private WorkingSetStore workingSetStore;

    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public List<String> getWorkingSetsOrderedByName() {
        try {
            return workingSetStore.getWorkingSetsOrderedByName();
        } catch (StoreException e) {
        	log.error(COULD_NOT_RETRIEVE_WORKING_SET_LIST, e);
            throw new RuntimeException(COULD_NOT_RETRIEVE_WORKING_SET_LIST);
        }
    }

    public boolean containsWorkingSet(String name) {
        try {
            return workingSetStore.getWorkingSet(name) != null;
        } catch (StoreException e) {
        	log.error(COULD_NOT_ASK_WORKING_SET, e);
            throw new RuntimeException(COULD_NOT_ASK_WORKING_SET);
        }
    }

    public WorkingSetData getWorkingSet(String name) {
        try {
            WorkingSet workingSet = workingSetStore.getWorkingSet(name);
            WorkingSetData wsd = new WorkingSetData();
            wsd.setIsNew(false);
            wsd.setNewName(workingSet.getName());
            wsd.setOldName(workingSet.getName());
            wsd.setSites(new ArrayList<String>(workingSet.getSites()));
            return wsd;
        } catch (StoreException e) {
        	log.error(COULD_NOT_RETRIEVE_WORKING_SET, e);
            throw new RuntimeException(COULD_NOT_RETRIEVE_WORKING_SET);
        }
    }
    
    public void update(WorkingSetData wsd) {
        try {
            String newName = wsd.getNewName();
            String oldName = wsd.getOldName();
            if (workingSetStore.getWorkingSet(newName) != null) {
                throw new IllegalArgumentException("Working set with name " + newName + " already exists!");
            }
            if (!wsd.getIsNew() && !newName.equals(oldName)) {
                workingSetStore.rename(oldName, newName);
            }
            WorkingSet workingSet = null;
            if (wsd.getIsNew()) {
            	workingSet = new WorkingSet();
            	workingSet.setName(newName);
            } else {
            	workingSet = workingSetStore.getWorkingSet(newName);
            }
            workingSet.setSites(wsd.getSites());
            workingSetStore.update(workingSet);
        } catch (StoreException e) {
        	log.error(COULD_NOT_UPDATE_WORKING_SET, e);
            throw new RuntimeException(COULD_NOT_UPDATE_WORKING_SET);
        }
    }
}
