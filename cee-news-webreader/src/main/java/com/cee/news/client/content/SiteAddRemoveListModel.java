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
import com.cee.news.client.list.ListChangedEvent;
import com.cee.news.client.list.SelectionListChangedEvent;
import com.cee.news.client.list.SelectionListChangedHandler;

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
    
    public void addSelection(int index) {
        selections.add(index);
        fireSelectionListChanged();
    }

    public void removeSelection(int index) {
        selections.remove(index);
        fireSelectionListChanged();
    }
    
    public Set<Integer> getSelections() {
        return selections;
    }
    
    protected void fireSelectionListChanged() {
        List<LinkValue> selectionLinks = new ArrayList<LinkValue>();
        for (int index : selections) {
            selectionLinks.add(new LinkValue(index, getSite(index)));
        }
        handlerManager.fireEvent(new SelectionListChangedEvent(selectionLinks));
    }

    public void addSelectionListChangedHandler(SelectionListChangedHandler handler) {
        handlerManager.addHandler(SelectionListChangedEvent.TYPE, handler);
    }
}
