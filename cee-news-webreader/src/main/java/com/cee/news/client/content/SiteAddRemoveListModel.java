/**
 * 
 */
package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cee.news.client.list.AddRemoveListModel;
import com.cee.news.client.list.LinkValue;
import com.cee.news.client.list.SelectionListChangedEvent;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Implementation of the {@link AddRemoveListModel} for selecting sites.
 */
public class SiteAddRemoveListModel extends SiteListContentModel implements AddRemoveListModel {

    protected Set<Integer> selections = new HashSet<Integer>();
    
    @Override
    public void updateSites() {
        super.updateSites();
        selections.clear();
        fireSelectionListChanged();
    }
    
    @Override
    public void addSelection(int index) {
        selections.add(index);
        fireSelectionListChanged();
    }
    
    @Override
    public void removeSelection(int index) {
        selections.remove(index);
        fireSelectionListChanged();
    }
    
    @Override
    public void setSelections(List<String> selectedSites) {
    	selections.clear();
    	if (selectedSites != null) {
	    	for (String site : selectedSites) {
				selections.add(sites.indexOf(site));
			}
    	}
    	fireSelectionListChanged();
    }
    
    @Override
    public void clearSelection() {
    	selections.clear();
    	fireSelectionListChanged();
    }
    
    protected void fireSelectionListChanged() {
        List<LinkValue> selectionLinks = new ArrayList<LinkValue>();
        for (int index : selections) {
            selectionLinks.add(new LinkValue(index, getSite(index)));
        }
        handlerManager.fireEvent(new SelectionListChangedEvent(selectionLinks));
    }

    @Override
    public HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler handler) {
        return handlerManager.addHandler(SelectionListChangedEvent.TYPE, handler);
    }
}
