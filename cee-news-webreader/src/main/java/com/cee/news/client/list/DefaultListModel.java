package com.cee.news.client.list;

import java.util.List;

import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract base implementation of the {@link ContentListModel} which has support for all
 * event handlers and the content selection. Implementations must call fireContentListChanged(List<LinkValue> links)
 * after initialization and every time the underlying model changes.
 */
public abstract class DefaultListModel extends ErrorSourceBase implements ListModel {

    protected int selection;
    
    public int getSelectedContent() {
        return selection;
    }

    public void setSelectedContent(int index) {
        this.selection = index;
        fireSelectionChanged();
    }

    public HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler handler) {
        return handlerManager.addHandler(SelectionChangedEvent.TYPE, handler);
    }

    public HandlerRegistration addListChangedHandler(ListChangedHandler handler) {
        return handlerManager.addHandler(ListChangedEvent.TYPE, handler);
    }
    
    protected void fireContentListChanged(List<LinkValue> links) {
        handlerManager.fireEvent(new ListChangedEvent(links));
    }
    
    protected void fireSelectionChanged() {
        handlerManager.fireEvent(new SelectionChangedEvent(selection));
    }
}
