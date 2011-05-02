package com.cee.news.client.workingset;

import com.cee.news.client.list.ListView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Provides a view for editing a working set.
 */
public interface WorkingSetView {

    HasClickHandlers getAddNewSiteButton();
    
    HasClickHandlers getRemoveAllSitesButton();
    
    HasClickHandlers getSaveButton();
    
    HasClickHandlers getCancelButton();
    
    HasValue<String> getNameTextBox();
 
    HasText getErrorMessageLabel();
    
    ListView getSiteListView();
    
    ListView getSelectedSitesListView();
}
