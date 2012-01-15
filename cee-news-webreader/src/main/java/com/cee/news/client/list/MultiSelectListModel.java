package com.cee.news.client.list;

import java.util.Collection;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Model of a selectable list. Multiple elements of the content list can be selected.
 */
public interface MultiSelectListModel extends ListModel {
    
    /**
     * Adds the element with key to the selection list
     * @param key The key of the element to be added
     */
    void addSelection(String key);
    
    /**
     * Removes the element with key from the selection list
     * @param key The key of the element to be removed
     */
    void removeSelection(String key);
    
    /**
     * Set the selected keys
     * @param selections List containing the selected keys
     */
    void setSelections(Collection<String> selections);
    
    /**
     * Clears the current selection
     */
    void clearSelection();
    
    /**
     * @param handler will be notified if the selection list is changed.
     */
    HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler handler);
}
