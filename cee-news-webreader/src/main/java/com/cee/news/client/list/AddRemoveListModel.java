package com.cee.news.client.list;

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
     * @param handler will be notified if the selection list is changed.
     */
    void addSelectionListChangedHandler(SelectionListChangedHandler handler);
}
