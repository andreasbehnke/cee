package com.cee.news.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVerticalScrolling;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * {@link HasVerticalScrolling} implementation for the window's body element
 * @author andreasbehnke
 *
 */
public class WindowVerticalScroll implements HasVerticalScrolling {
    
    private final int offset;
    
    private final Element element;
    
    public WindowVerticalScroll() {
        offset = 0;
        element = RootPanel.getBodyElement();
    }
    
    public WindowVerticalScroll(Element element, int offset) {
        this.element = element;
        this.offset = offset;
    }

    @Override
    public int getMaximumVerticalScrollPosition() {
        int scrollHeight = element.getScrollHeight() + offset;
        int clientHeight = Window.getClientHeight();
        
        return scrollHeight - clientHeight;
    }

    @Override
    public int getMinimumVerticalScrollPosition() {
        return 0;
    }

    @Override
    public int getVerticalScrollPosition() {
        return Window.getScrollTop();
    }

    @Override
    public void setVerticalScrollPosition(int position) {
        //not supported
    }
}