package com.cee.news.client.content;

import com.cee.news.client.list.EntityContent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;

public interface SourceSelectionView {
    
    CellList<EntityContent> getCellListSites();

    HasClickHandlers getSelectAllButton();
    
    HasClickHandlers getSelectNoneButton();
    
    HasClickHandlers getAddButton();
}
