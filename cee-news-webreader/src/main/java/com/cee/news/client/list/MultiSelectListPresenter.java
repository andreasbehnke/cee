package com.cee.news.client.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasEnabled;

public class MultiSelectListPresenter {
    
    private final MultiSelectListModel multiModel;
    
    private final ContentModel contentModel;
    
    private final ListView sourceView;
    
    private final ListView selectionView;
    
    private final Map<String, HasEnabled> sourceItems;

    public MultiSelectListPresenter(final MultiSelectListModel multiModel, final ContentModel contentModel, final ListView sourceView, final ListView selectionView) {
        this.multiModel = multiModel;
        this.contentModel = contentModel;
        this.sourceView = sourceView;
        this.selectionView = selectionView;
        this.sourceItems = new HashMap<String, HasEnabled>();
        
        multiModel.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillSourceList(event.getLinks());
            }
        });
        
        multiModel.addSelectionListChangedHandler(new SelectionListChangedHandler() {
            
            public void onSelectionListChanged(SelectionListChangedEvent event) {
                fillSelectionList(event.getLinks());
            }
        });
    }
    
    protected void fillSourceList(List<EntityKey> links) {
        sourceView.removeAll();
        sourceItems.clear();
        for (final EntityKey link : links) {
            String key = link.getKey();
            ListItemView item = sourceView.addItem();
            contentModel.getContentDescription(item, key);
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    addSelection(link);
                }
            });
            sourceItems.put(link.getKey(), item);
        }
    }
    
    protected void fillSelectionList(List<EntityKey> links) {
        selectionView.removeAll();
        for (Entry<String, HasEnabled> entry : sourceItems.entrySet()) {
			entry.getValue().setEnabled(true);
		}
        for (final EntityKey link : links) {
            ListItemView item = selectionView.addItem();
            contentModel.getContentDescription(item, link.getKey());
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    removeSelection(link);
                }
            });
            sourceItems.get(link.getKey()).setEnabled(false);
        }
    }
    
    protected void addSelection(EntityKey link) {
        String key = link.getKey();
        multiModel.addSelection(key);
        sourceItems.get(key).setEnabled(false);
    }

    protected void removeSelection(EntityKey link) {
        String key = link.getKey();
        multiModel.removeSelection(key);
        sourceItems.get(key).setEnabled(true);
    }
}
