package com.cee.news.client.list;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class ListPresenter {

    private final ListView view;
    
    private final ContentModel contentModel;
    
    private final ListModel listModel;

    public ListPresenter(final ListModel listModel, final ContentModel contentModel, final ListView view) {
        this.view = view;
        this.contentModel = contentModel;
        this.listModel = listModel;
        listModel.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillList(event.getLinks());
            }
        });
    }
    
    protected void fillList(List<EntityKey> links) {
        view.removeAll();
        for (final EntityKey link : links) {
            ListItemView item = view.addItem();
            contentModel.getContentDescription(item, link.getKey());
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    listModel.userSelectedKey(link.getKey());
                }
            });
        }
    }
}
