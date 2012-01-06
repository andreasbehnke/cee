package com.cee.news.client.paging;

import java.util.List;

import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.EntityKeyUtil;
import com.cee.news.client.list.ListChangedEvent;
import com.cee.news.client.list.ListChangedHandler;
import com.cee.news.client.list.ListModel;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Presenter handles {@link PagingContentModel} events and fills the {@link PagingView} with content.
 * Listens for {@link PagingView} and notifies the {@link PagingContentModel} about changes.
 */
public class PagingPresenter {

	private final ListModel listModel;
	
    private final ContentModel contentModel;

    private final PagingView view;
    
    private List<EntityKey> keys;

    public PagingPresenter( final ListModel listModel, final ContentModel contentModel, final PagingView view) {
        this.listModel = listModel;
    	this.contentModel = contentModel;
        this.view = view;

        listModel.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillJumpToList(event.getLinks());
            }
        });
        
        listModel.addSelectionChangedhandler(new SelectionChangedHandler() {
            public void onSelectionChange(SelectionChangedEvent event) {
                onSelectionChanged(event.getKey());
            }
        });
        
        view.addJumpToChangedHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int index = view.getJumpToSelectedIndex();
                String key = keys.get(index).getKey();
                listModel.userSelectedKey(key);
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

    protected void fillJumpToList(List<EntityKey> keys) {
    	this.keys = keys;
        view.setJumpToLinks(keys);
    }
    
    protected void onSelectionChanged(String key) {
    	int index = EntityKeyUtil.getIndexOfEntityKey(keys, key);
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
                String prevKey = keys.get(index - 1).getKey();
                contentModel.getContentTitle(view.getPreviousContent(), prevKey);
            }
            if (index == listModel.getContentCount() - 1) {
                view.setNextEnabled(false);
            } else {
                view.setNextEnabled(true);
                String nextKey = keys.get(index + 1).getKey();
                contentModel.getContentTitle(view.getNextContent(), nextKey);
            }
    	}
        contentModel.getContent(view.getMainContent(), key);
        view.resetMainContentScrollPosition();
    }
    
    protected void increment() {
        int currentSelection = EntityKeyUtil.getIndexOfEntityKey(keys, listModel.getSelectedKey());
        if (currentSelection < listModel.getContentCount() - 1) {
        	listModel.userSelectedKey(keys.get(currentSelection + 1).getKey());
        }
    }
    
    protected void decrement() {
    	int currentSelection = EntityKeyUtil.getIndexOfEntityKey(keys, listModel.getSelectedKey());
        if (currentSelection > 0) {
        	listModel.userSelectedKey(keys.get(currentSelection - 1).getKey());
        }
    }
}
