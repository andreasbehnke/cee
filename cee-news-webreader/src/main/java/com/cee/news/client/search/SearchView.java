package com.cee.news.client.search;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface SearchView {
    
    HasClickHandlers getSearchButton();
    
    HasClickHandlers getClearButton();
    
    HasText getSearchText();

    void setSearchButtonEnabled(boolean enabled);
    
    void setClearButtonEnabled(boolean enabled);
}
