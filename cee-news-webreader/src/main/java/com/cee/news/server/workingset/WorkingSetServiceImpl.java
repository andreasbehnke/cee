package com.cee.news.server.workingset;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.workingset.WorkingSetData;
import com.cee.news.client.workingset.WorkingSetService;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class WorkingSetServiceImpl implements WorkingSetService {

    private WorkingSetStore workingSetStore;

    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public List<String> getWorkingSetsOrderedByName() {
        try {
            return workingSetStore.getWorkingSetsOrderedByName();
        } catch (StoreException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsWorkingSet(String name) {
        try {
            return workingSetStore.getWorkingSet(name) != null;
        } catch (StoreException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
}
