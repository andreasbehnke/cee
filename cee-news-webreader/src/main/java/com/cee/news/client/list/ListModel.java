package com.cee.news.client.list;

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
     */
    void addSelectionChangedhandler(SelectionChangedHandler handler);

    /**
     * @param handler will be notified if the content model list is changed.
     */
    void addListChangedHandler(ListChangedHandler handler);

}
