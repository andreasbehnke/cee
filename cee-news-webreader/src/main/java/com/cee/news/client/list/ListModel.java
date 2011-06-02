package com.cee.news.client.list;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ListModel {
    /**
     * @return The number of content available
     */
    int getContentCount();

    /**
     * @return The index of the current content
     */
    int getSelectedContent();

    /**
     * @param index the current selected index
     */
    void setSelectedContent(int index);

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
