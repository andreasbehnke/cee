package org.cee.webreader.client.list;

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
