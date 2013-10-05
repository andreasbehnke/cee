package org.cee.webreader.client.search;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface SearchView extends IsWidget {
    
    HasClickHandlers getSearchButton();
    
    HasClickHandlers getClearButton();
    
    HasText getSearchText();

    void setSearchButtonEnabled(boolean enabled);
    
    void setClearButtonEnabled(boolean enabled);
}
