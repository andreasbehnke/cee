package com.cee.news.client.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasEnabled;

public class AddRemoveListPresenter {
    
    private final AddRemoveListModel model;
    
    private final ListView sourceView;
    
    private final ListView selectionView;
    
    private final Map<Integer, HasEnabled> sourceItems;

    public AddRemoveListPresenter(final AddRemoveListModel model, final ListView sourceView, final ListView selectionView) {
        this.model = model;
        this.sourceView = sourceView;
        this.selectionView = selectionView;
        this.sourceItems = new HashMap<Integer, HasEnabled>();
        
        model.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillSourceList(event.getLinks());
            }
        });
        
        model.addSelectionListChangedHandler(new SelectionListChangedHandler() {
            
            public void onSelectionListChanged(SelectionListChangedEvent event) {
                fillSelectionList(event.getLinks());
            }
        });
    }
    
    protected void fillSourceList(List<LinkValue> links) {
        sourceView.removeAll();
        sourceItems.clear();
        for (final LinkValue link : links) {
            int index = link.getValue();
            ListItemView item = sourceView.addItem();
            model.getContentDescription(item, index);
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    addSelection(link);
                }
            });
            sourceItems.put(link.getValue(), item);
        }
    }
    
    protected void fillSelectionList(List<LinkValue> links) {
        selectionView.removeAll();
        for (Entry<Integer, HasEnabled> entry : sourceItems.entrySet()) {
			entry.getValue().setEnabled(true);
		}
        for (final LinkValue link : links) {
            ListItemView item = selectionView.addItem();
            model.getContentDescription(item, link.getValue());
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    removeSelection(link);
                }
            });
            sourceItems.get(link.getValue()).setEnabled(false);
        }
    }
    
    protected void addSelection(LinkValue link) {
        int index = link.getValue();
        model.addSelection(link.getValue());
        sourceItems.get(index).setEnabled(false);
    }

    protected void removeSelection(LinkValue link) {
        int index = link.getValue();
        model.removeSelection(index);
        sourceItems.get(index).setEnabled(true);
    }
}
