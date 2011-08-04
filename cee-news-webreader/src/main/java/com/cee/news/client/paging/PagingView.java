package com.cee.news.client.paging;

import java.util.List;

import com.cee.news.client.list.EntityKey;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.HasSafeHtml;

/**
 * A pageable view displays content and links for navigating to the previous or next content.
 * An additional content selection for direct jumps is also available.
 */
public interface PagingView {

    /**
     * @param enabled If true, the previous link is enabled
     */
    void setPreviousEnabled(boolean enabled);
    
    /**
     * @return The html content of the previous link
     */
    HasSafeHtml getPreviousContent();
    
    /**
     * @param handler Handler will be notified if the previous link is clicked
     */
    void addPreviousClickHandler(ClickHandler handler);
    
    /**
     * @param enabled If true, the next link is enabled
     */
    void setNextEnabled(boolean enabled);
    
    /**
     * @return The html content of the next link
     */
    HasSafeHtml getNextContent();
    
    /**
     * @param handler Handler will be notified if the next link is clicked
     */
    void addNextClickHandler(ClickHandler handler);
    
    /**
     * @return The html content of the main content area
     */
    HasSafeHtml getMainContent();
    
    /**
     * @param links The list of links which should be displayed in e.g. a selection box
     */
    void setJumpToLinks(List<EntityKey> links);
    
    /**
     * @param index The currently selected index of the selection box
     */
    void setJumpToSelectedIndex(int index);
    
    int getJumpToSelectedIndex();
    
    /**
     * @param handler Handler will be notified if the user selected another content from the selection box
     */
    void addJumpToChangedHandler(ChangeHandler handler);
}
