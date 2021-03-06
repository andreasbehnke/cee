package org.cee.webreader.client.paging;

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


import java.util.ArrayList;
import java.util.List;

import org.cee.webreader.client.list.ContentModel;
import org.cee.webreader.client.list.KeyLink;
import org.cee.webreader.client.list.KeyLinkResolver;
import org.cee.webreader.client.list.ListChangedEvent;
import org.cee.webreader.client.list.ListChangedHandler;
import org.cee.webreader.client.list.ListModel;
import org.cee.webreader.client.list.SelectionChangedEvent;
import org.cee.webreader.client.list.SelectionChangedHandler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Presenter handles {@link PagingContentModel} events and fills the {@link PagingView} with content.
 * Listens for {@link PagingView} and notifies the {@link PagingContentModel} about changes.
 */
public class PagingPresenter<K> {

	private final ListModel<K> listModel;
	
	private final KeyLinkResolver<K> linkResolver;
	
    private final ContentModel<K> contentModel;

    private final PagingView view;
    
    public PagingPresenter(final ListModel<K> listModel, final ContentModel<K> contentModel, final KeyLinkResolver<K> linkResolver, final PagingView view) {
        this.listModel = listModel;
    	this.contentModel = contentModel;
    	this.linkResolver = linkResolver;
        this.view = view;

        listModel.addListChangedHandler(new ListChangedHandler<K>() {
            public void onContentListChanged(ListChangedEvent<K> event) {
                fillJumpToList(event.getValues());
            }
        });
        
        listModel.addSelectionChangedhandler(new SelectionChangedHandler<K>() {
            public void onSelectionChange(SelectionChangedEvent<K> event) {
                onSelectionChanged(event.getKey());
            }
        });
        
        view.addJumpToChangedHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int index = view.getJumpToSelectedIndex();
                listModel.userSelectedKey(listModel.getKey(index));
            }
        });
        
        view.addNextClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                increment();
            }
        });
        
        view.addPreviousClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                decrement();
            }
        });
    }

    protected void fillJumpToList(List<K> keys) {
    	List<KeyLink> links = new ArrayList<KeyLink>();
        for (K key : keys) {
            links.add(linkResolver.createLink(key));
        }
        view.setJumpToLinks(links);
    }
    
    protected void onSelectionChanged(K key) {
    	int index = listModel.getIndexOf(key);
    	if (index == -1) {
    	    //the key is no element of this list. display article and disable next / prev buttons
    	    view.setPreviousEnabled(false);
    	    view.setNextEnabled(false);
    	} else {
        	view.setJumpToSelectedIndex(index);
            if (index == 0) {
                view.setPreviousEnabled(false);
            } else {
                view.setPreviousEnabled(true);
                K prevKey = listModel.getKey(index - 1);
                contentModel.getContentTitle(view.getPreviousContent(), prevKey);
            }
            if (index == listModel.getContentCount() - 1) {
                view.setNextEnabled(false);
            } else {
                view.setNextEnabled(true);
                K nextKey = listModel.getKey(index + 1);
                contentModel.getContentTitle(view.getNextContent(), nextKey);
            }
    	}
        contentModel.getContent(view.getMainContent(), key);
        view.resetMainContentScrollPosition();
    }
    
    protected void increment() {
        int index = listModel.getIndexOf(listModel.getSelectedKey());
        if (index < listModel.getContentCount() - 1) {
        	listModel.userSelectedKey(listModel.getKey(index + 1));
        }
    }
    
    protected void decrement() {
        int index = listModel.getIndexOf(listModel.getSelectedKey());
        if (index > 0) {
            listModel.userSelectedKey(listModel.getKey(index - 1));
        }
    }
}
