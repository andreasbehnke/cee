package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface WorkingSetSelectionView {

    void addSelectionChangedHandler(ChangeHandler handler);
    
    HasClickHandlers getNewButton();
    
    HasClickHandlers getEditButton();
    
    void setEditButtonEnabled(boolean enabled);
    
    int getSelectedWorkingSet();
    
    void setSelectedWorkingSet(int key);
    
    void setWorkingSets(List<EntityKey> names);
}
