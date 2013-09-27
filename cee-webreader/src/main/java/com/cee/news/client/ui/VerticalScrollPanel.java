package com.cee.news.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasVerticalScrolling;
import com.google.gwt.user.client.ui.SimplePanel;

public class VerticalScrollPanel extends SimplePanel implements HasVerticalScrolling, HasScrollHandlers {

    @Override
    public int getMaximumVerticalScrollPosition() {
        Element elm = getElement();
        return elm.getScrollHeight() - elm.getClientHeight(); 
    }

    @Override
    public int getMinimumVerticalScrollPosition() {
        return 0;
    }

    @Override
    public int getVerticalScrollPosition() {
        return getElement().getScrollTop();
    }

    @Override
    public void setVerticalScrollPosition(int position) {
        getElement().setScrollTop(position);
    }

    @Override
    public HandlerRegistration addScrollHandler(ScrollHandler handler) {
        Event.sinkEvents(getElement(), Event.ONSCROLL);
        return addHandler(handler, ScrollEvent.getType());
    }

}
