package com.cee.news.client.content;

import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.IsWidget;

public interface SourceSelectionView extends IsWidget {
    
    CellList<EntityKey> getCellListSites();

    HasClickHandlers getSelectAllButton();
    
    HasClickHandlers getSelectNoneButton();
    
    HasClickHandlers getAddButton();
}
