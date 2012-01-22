package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract base implementation of the {@link ContentModel} which has support for all
 * event handlers and the content selection. Implementations must call fireContentListChanged(List<LinkValue> links)
 * after initialization and every time the underlying model changes.
 */
public abstract class DefaultListModel<K> extends ErrorSourceBase implements MultiSelectListModel<K> {

	protected List<K> keys;
    
	protected K selectedKey;
    
    protected Set<K> selectedKeys = new HashSet<K>();

    @Override
    public K getSelectedKey() {
        return selectedKey;
    }

    @Override
    public void setSelectedKey(K key) {
        this.selectedKey = key;
        fireSelectionChanged(false);
    }
    
    @Override
    public void userSelectedKey(K key) {
    	this.selectedKey = key;
        fireSelectionChanged(true);
    }
    
    @Override
    public int getContentCount() {
    	if (keys == null) {
    		return 0;
    	}
    	return keys.size();
    }
    
    @Override
    public int getIndexOf(K key) {
        if (keys == null) {
            return -1;
        }
        return keys.indexOf(key);
    };
    
    @Override
    public K getKey(int index) {
        if (keys == null) {
            return null;
        }
        return keys.get(index);
    }
    
    @Override
    public void addSelection(K key) {
        selectedKeys.add(key);
        fireSelectionListChanged();
    }
    
    @Override
    public void removeSelection(K key) {
        selectedKeys.remove(key);
        fireSelectionListChanged();
    }
    
    @Override
    public void setSelections(Collection<K> selectedKeys) {
    	this.selectedKeys.clear();
    	this.selectedKeys.addAll(selectedKeys);
    	fireSelectionListChanged();
    }
    
    @Override
    public void clearSelection() {
    	this.selectedKeys.clear();
    	fireSelectionListChanged();
    }
    
    @Override
    public HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler<K> handler) {
        return handlerManager.addHandler(SelectionListChangedEvent.TYPE, handler);
    }
    
    @Override
    public HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler<K> handler) {
        return handlerManager.addHandler(SelectionChangedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addListChangedHandler(ListChangedHandler<K> handler) {
        return handlerManager.addHandler(ListChangedEvent.TYPE, handler);
    }
    
    @Override
    public List<K> getKeys() {
    	return keys;
    }
    
    protected void setValues(List<K> keys) {
    	this.keys = keys;
    	fireContentListChanged(keys);
    }
    
    protected void fireContentListChanged(List<K> links) {
        handlerManager.fireEvent(new ListChangedEvent<K>(links));
    }
    
    protected void fireSelectionChanged(boolean userAction) {
        handlerManager.fireEvent(new SelectionChangedEvent<K>(selectedKey, userAction));
    }
    
    protected void fireSelectionListChanged() {
        handlerManager.fireEvent(new SelectionListChangedEvent<K>(new ArrayList<K>(selectedKeys)));
    }
}
