package com.cee.news.client.list;

import java.util.List;

import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.error.ErrorSource;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Abstract base implementation of the {@link ContentListModel} which has support for all
 * event handlers and the content selection. Implementations must call fireContentListChanged(List<LinkValue> links)
 * after initialization and every time the underlying model changes.
 */
public abstract class DefaultListModel implements ListModel, ErrorSource {

    protected EventBus handlerManager = new SimpleEventBus();
    
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
    
    public HandlerRegistration addErrorHandler(ErrorHandler handler) {
        return handlerManager.addHandler(ErrorEvent.TYPE, handler);
    }
    
    protected void fireContentListChanged(List<LinkValue> links) {
        handlerManager.fireEvent(new ListChangedEvent(links));
    }
    
    protected void fireSelectionChanged() {
        handlerManager.fireEvent(new SelectionChangedEvent(selection));
    }

    protected void fireErrorEvent(Throwable cause, String description) {
        handlerManager.fireEvent(new ErrorEvent(cause, description));
    }
}
