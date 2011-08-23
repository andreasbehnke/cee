package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cee.news.client.error.ErrorSourceBase;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract base implementation of the {@link ContentModel} which has support for all
 * event handlers and the content selection. Implementations must call fireContentListChanged(List<LinkValue> links)
 * after initialization and every time the underlying model changes.
 */
public abstract class DefaultListModel extends ErrorSourceBase implements MultiSelectListModel {

	protected List<EntityKey> keys;
    
	protected String selectedKey;
    
    protected Set<String> selectedKeys = new HashSet<String>();
    
    @Override
    public String getSelectedKey() {
        return selectedKey;
    }

    @Override
    public void setSelectedKey(String key) {
        this.selectedKey = key;
        fireSelectionChanged();
    }
    
    @Override
    public int getContentCount() {
    	if (keys == null) {
    		return 0;
    	}
    	return keys.size();
    }
    
    @Override
    public void addSelection(String key) {
        selectedKeys.add(key);
        fireSelectionListChanged();
    }
    
    @Override
    public void removeSelection(String key) {
        selectedKeys.remove(key);
        fireSelectionListChanged();
    }
    
    @Override
    public void setSelections(List<String> selectedKeys) {
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
    public HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler handler) {
        return handlerManager.addHandler(SelectionListChangedEvent.TYPE, handler);
    }
    
    @Override
    public HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler handler) {
        return handlerManager.addHandler(SelectionChangedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addListChangedHandler(ListChangedHandler handler) {
        return handlerManager.addHandler(ListChangedEvent.TYPE, handler);
    }
    
    protected void setKeys(List<EntityKey> keys) {
    	this.keys = keys;
    	fireContentListChanged(keys);
        if (keys != null && !keys.isEmpty()) {
        	setSelectedKey(keys.get(0).getKey());
        }
        selectedKeys.clear();
        fireSelectionListChanged();
    }
    
    protected void fireContentListChanged(List<EntityKey> links) {
        handlerManager.fireEvent(new ListChangedEvent(links));
    }
    
    protected void fireSelectionChanged() {
        handlerManager.fireEvent(new SelectionChangedEvent(selectedKey));
    }
    
    protected void fireSelectionListChanged() {
        List<EntityKey> selectionLinks = new ArrayList<EntityKey>();
        for (String key : selectedKeys) {
            selectionLinks.add(EntityKeyUtil.getEntityKey(keys, key));
        }
        handlerManager.fireEvent(new SelectionListChangedEvent(selectionLinks));
    }
}
