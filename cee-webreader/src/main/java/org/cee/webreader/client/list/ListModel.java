package org.cee.webreader.client.list;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ListModel<K> {
	
    /**
     * @return the list of all keys
     */
    List<K> getKeys();
    
    /**
     * @return the key at index position
     */
    K getKey(int index);

    /**
     * @return The number of content available
     */
    int getContentCount();
    
    /**
     * @return index of key or -1 if key is no element of this model
     */
    int getIndexOf(K key);
    
    /**
     * @return The primary key of the current content
     */
    K getSelectedKey();

    /**
     * @param key the current selected entity
     */
    void setSelectedKey(K key);
    
    /**
     * This method is called by presenters if the user selected an item
     * from a list. In difference to setSelectedKey the flag userAction 
     * of the generated event is set to true.
     * @param key The key of the user selected item
     */
    void userSelectedKey(K key);

    /**
     * @param handler will be notified about changes of the selected index
     * @return 
     */
    HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler<K> handler);

    /**
     * @param handler will be notified if the content model list is changed.
     * @return 
     */
    HandlerRegistration addListChangedHandler(ListChangedHandler<K> handler);

}
