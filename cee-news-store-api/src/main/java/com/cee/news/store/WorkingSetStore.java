package com.cee.news.store;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;

public interface WorkingSetStore {

    EntityKey update(WorkingSet workingSet) throws StoreException;
    
    void rename(String oldName, String newName) throws StoreException;
    
    boolean contains(String name) throws StoreException;
    
    WorkingSet getWorkingSet(String name) throws StoreException;
    
    long getWorkingSetCount() throws StoreException;
    
    List<EntityKey> getWorkingSetsOrderedByName() throws StoreException;
}
