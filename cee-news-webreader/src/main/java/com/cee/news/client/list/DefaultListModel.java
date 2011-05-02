package com.cee.news.client.list;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Abstract base implementation of the {@link ContentListModel} which has support for all
 * event handlers and the content selection. Implementations must call fireContentListChanged(List<LinkValue> links)
 * after initialization and every time the underlying model changes.
 */
public abstract class DefaultListModel implements ListModel {

    protected EventBus handlerManager = new SimpleEventBus();
    
    protected int selection;
    
    public int getSelectedContent() {
        return selection;
    }

    public void setSelectedContent(int index) {
        this.selection = index;
        fireSelectionChanged();
    }

    public void addSelectionChangedhandler(SelectionChangedHandler handler) {
        handlerManager.addHandler(SelectionChangedEvent.TYPE, handler);
    }

    public void addListChangedHandler(ListChangedHandler handler) {
        handlerManager.addHandler(ListChangedEvent.TYPE, handler);
    }

    protected void fireContentListChanged(List<LinkValue> links) {
        handlerManager.fireEvent(new ListChangedEvent(links));
    }
    
    protected void fireSelectionChanged() {
        handlerManager.fireEvent(new SelectionChangedEvent(selection));
    }
}
