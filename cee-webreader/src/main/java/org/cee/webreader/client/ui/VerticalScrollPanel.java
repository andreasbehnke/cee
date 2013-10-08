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
