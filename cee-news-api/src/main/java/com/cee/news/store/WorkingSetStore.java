package com.cee.news.store;

import java.util.List;

import com.cee.news.model.WorkingSet;

public interface WorkingSetStore {

    void update(WorkingSet workingSet) throws StoreException;
    
    void rename(String oldName, String newName) throws StoreException;
    
    WorkingSet getWorkingSet(String name) throws StoreException;
    
    long getWorkingSetCount() throws StoreException;
    
    List<String> getWorkingSetsOrderedByName() throws StoreException;
}
