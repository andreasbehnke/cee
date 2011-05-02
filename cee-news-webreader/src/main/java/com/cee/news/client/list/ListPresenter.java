package com.cee.news.client.list;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class ListPresenter {

    private final ListView view;
    
    private final ContentListModel model;

    public ListPresenter(final ContentListModel model, final ListView view) {
        this.view = view;
        this.model = model;
        model.addListChangedHandler(new ListChangedHandler() {
            public void onContentListChanged(ListChangedEvent event) {
                fillList(event.getLinks());
            }
        });
    }
    
    protected void fillList(List<LinkValue> links) {
        view.removeAll();
        for (final LinkValue link : links) {
            ListItemView item = view.addItem();
            model.getContentDescription(item, link.getValue());
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    model.setSelectedContent(link.getValue());
                }
            });
        }
    }
}
