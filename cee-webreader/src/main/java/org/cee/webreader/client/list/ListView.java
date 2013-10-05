package org.cee.webreader.client.list;


/**
 * Represents a scrollable and clickable list of content headlines and descriptions
 */
public interface ListView {

    /**
     * Adds an empty item to the end of list
     */
    ListItemView addItem();
    
    /**
     * Removes all items from this list
     */
    void removeAll();
}