package org.cee.webreader.client.workingset;

import java.util.List;

import org.cee.news.model.EntityKey;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface WorkingSetSelectionView {

    void addSelectionChangedHandler(ChangeHandler handler);
    
    HasClickHandlers getNewButton();
    
    HasClickHandlers getEditButton();
    
    HasClickHandlers getDeleteButton();
    
    void setEditButtonEnabled(boolean enabled);
    
    void setDeleteButtonEnabled(boolean enabled);
    
    int getSelectedWorkingSet();
    
    void setSelectedWorkingSet(int key);
    
    void setWorkingSets(List<EntityKey> names);
}
