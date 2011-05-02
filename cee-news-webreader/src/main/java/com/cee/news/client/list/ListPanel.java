package com.cee.news.client.list;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPanel extends ScrollPanel implements ListView {
    
    private final VerticalPanel list;
    
    public ListPanel() {
        list = new VerticalPanel();
        setWidget(list);
    }

    public ListItemView addItem() {
        ListItemButton button = new ListItemButton();
        button.setWidth("100%");
        button.setHeight("80px");
        list.add(button);
        return button;
    }

    public void removeAll() {
        list.clear();
    }
}
