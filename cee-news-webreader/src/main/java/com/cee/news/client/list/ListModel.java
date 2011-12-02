package com.cee.news.client.list;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.event.shared.HandlerRegistration;

public interface ListModel {
	
	/**
     * @return the list of all keys
     */
    List<EntityKey> getKeys();

    /**
     * @return The number of content available
     */
    int getContentCount();
    
    /**
     * @return The primary key of the current content
     */
    String getSelectedKey();

    /**
     * @param key the current selected entity
     */
    void setSelectedKey(String key);
    
    /**
     * This method is called by presenters if the user selected an item
     * from a list. In difference to setSelectedKey the flag userAction 
     * of the generated event is set to true.
     * @param key The key of the user selected item
     */
    void userSelectedKey(String key);

    /**
     * @param handler will be notified about changes of the selected index
     * @return 
     */
    HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler handler);

    /**
     * @param handler will be notified if the content model list is changed.
     * @return 
     */
    HandlerRegistration addListChangedHandler(ListChangedHandler handler);

}
