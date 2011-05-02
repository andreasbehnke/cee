package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.list.LinkValue;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface WorkingSetSelectionView {

    void addSelectionChangedHandler(ChangeHandler handler);
    
    HasClickHandlers getNewButton();
    
    HasClickHandlers getEditButton();
    
    int getSelectedWorkingSet();
    
    void setSelectedWorkingSet(int index);
    
    void setWorkingSets(List<LinkValue> names);
}
