package org.cee.webreader.client.ui;

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