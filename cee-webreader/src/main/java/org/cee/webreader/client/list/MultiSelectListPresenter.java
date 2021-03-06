package org.cee.webreader.client.list;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasEnabled;

public class MultiSelectListPresenter<K> {
    
    private final MultiSelectListModel<K> multiModel;
    
    private final ContentModel<K> contentModel;
    
    private final ListView sourceView;
    
    private final ListView selectionView;
    
    private final Map<K, HasEnabled> sourceItems;

    public MultiSelectListPresenter(final MultiSelectListModel<K> multiModel, final ContentModel<K> contentModel, final ListView sourceView, final ListView selectionView) {
        this.multiModel = multiModel;
        this.contentModel = contentModel;
        this.sourceView = sourceView;
        this.selectionView = selectionView;
        this.sourceItems = new HashMap<K, HasEnabled>();
        
        multiModel.addListChangedHandler(new ListChangedHandler<K>() {
            public void onContentListChanged(ListChangedEvent<K> event) {
                fillSourceList(event.getValues());
            }
        });
        
        multiModel.addSelectionListChangedHandler(new SelectionListChangedHandler<K>() {
            public void onSelectionListChanged(SelectionListChangedEvent<K> event) {
                fillSelectionList(event.getKeys());
            }
        });
    }
    
    protected void fillSourceList(List<K> keys) {
        sourceView.removeAll();
        sourceItems.clear();
        for (final K key : keys) {
            ListItemView item = sourceView.addItem();
            contentModel.getContentDescription(item, key);
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    addSelection(key);
                }
            });
            sourceItems.put(key, item);
        }
    }
    
    protected void fillSelectionList(List<K> keys) {
        selectionView.removeAll();
        for (Entry<K, HasEnabled> entry : sourceItems.entrySet()) {
			entry.getValue().setEnabled(true);
		}
        for (final K key : keys) {
            ListItemView item = selectionView.addItem();
            contentModel.getContentDescription(item, key);
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    removeSelection(key);
                }
            });
            sourceItems.get(key).setEnabled(false);
        }
    }
    
    protected void addSelection(K key) {
        multiModel.addSelection(key);
        sourceItems.get(key).setEnabled(false);
    }

    protected void removeSelection(K key) {
        multiModel.removeSelection(key);
        sourceItems.get(key).setEnabled(true);
    }
}
