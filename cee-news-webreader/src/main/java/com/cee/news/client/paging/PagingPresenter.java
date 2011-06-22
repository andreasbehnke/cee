package com.cee.news.client.paging;

import java.util.List;

import com.cee.news.client.list.LinkValue;
import com.cee.news.client.list.ListChangedEvent;
import com.cee.news.client.list.ListChangedHandler;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Presenter handles {@link PagingContentModel} events and fills the {@link PagingView} with content.
 * Listens for {@link PagingView} and notifies the {@link PagingContentModel} about changes.
 */
public class PagingPresenter {

    private final PagingContentModel model;

    private final PagingView view;

    public PagingPresenter(final PagingContentModel model, final PagingView view) {
        this.model = model;
        this.view = view;

        model.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillJumpToList(event.getLinks());
            }
        });

        model.addSelectionChangedhandler(new SelectionChangedHandler() {
            public void onSelectionChange(SelectionChangedEvent event) {
                onSelectionChanged(event.getSelection());
            }
        });
        
        view.addJumpToChangedHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int selection = view.getJumpToSelectedIndex();
                if (selection > -1) {
                    model.setSelectedContent(selection);
                }
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

    protected void fillJumpToList(List<LinkValue> links) {
        view.setJumpToLinks(links);
    }
    
    protected void onSelectionChanged(int index) {
        view.setJumpToSelectedIndex(index);
        if (index == 0) {
            view.setPreviousEnabled(false);
        } else {
            view.setPreviousEnabled(true);
            model.getContentTitle(view.getPreviousContent(), index - 1);
        }
        if (index == model.getContentCount() - 1) {
            view.setNextEnabled(false);
        } else {
            view.setNextEnabled(true);
            model.getContentTitle(view.getNextContent(), index + 1);
        }
        model.getContent(view.getMainContent(), index);
    }
    
    protected void increment() {
        int currentSelection = model.getSelectedContent();
        if (currentSelection < model.getContentCount() - 1) {
            model.setSelectedContent(currentSelection + 1);
        }
    }
    
    protected void decrement() {
        int currentSelection = model.getSelectedContent();
        if (currentSelection > 0) {
            model.setSelectedContent(currentSelection - 1);
        }
    }
}
