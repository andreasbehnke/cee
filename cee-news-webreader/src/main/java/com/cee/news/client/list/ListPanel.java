package com.cee.news.client.list;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPanel extends SimplePanel implements ListView {
    
    private final VerticalPanel list;
    
    public ListPanel() {
        list = new VerticalPanel();
        list.setWidth("100%");
        setWidget(list);
    }

    public ListItemView addItem() {
        ListItemButton button = new ListItemButton();
        button.setWidth("100%");
        button.setHeight("80px");
        button.setStyleName("");
        list.add(button);
        return button;
    }

    public void removeAll() {
        list.clear();
    }
}
