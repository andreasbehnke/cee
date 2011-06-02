package com.cee.news.client.list;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Model of a selectable list. Multiple elements of the content list can be selected.
 */
public interface AddRemoveListModel extends ContentListModel {
    
    /**
     * Adds the element at index to the selection list
     * @param index The index of the element to be added
     */
    void addSelection(int index);
    
    /**
     * Removes the element at index from the selection list
     * @param index The index of the element to be removed
     */
    void removeSelection(int index);
    
    /**
     * Set the selected keys
     * @param selections List containing the selected keys
     */
    void setSelections(List<String> selections);
    
    /**
     * Clears the current selection
     */
    void clearSelection();
    
    /**
     * @param handler will be notified if the selection list is changed.
     */
    HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler handler);
}
