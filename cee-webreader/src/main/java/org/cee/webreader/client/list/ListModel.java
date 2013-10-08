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


import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ListModel<K> {
	
    /**
     * @return the list of all keys
     */
    List<K> getKeys();
    
    /**
     * @return the key at index position
     */
    K getKey(int index);

    /**
     * @return The number of content available
     */
    int getContentCount();
    
    /**
     * @return index of key or -1 if key is no element of this model
     */
    int getIndexOf(K key);
    
    /**
     * @return The primary key of the current content
     */
    K getSelectedKey();

    /**
     * @param key the current selected entity
     */
    void setSelectedKey(K key);
    
    /**
     * This method is called by presenters if the user selected an item
     * from a list. In difference to setSelectedKey the flag userAction 
     * of the generated event is set to true.
     * @param key The key of the user selected item
     */
    void userSelectedKey(K key);

    /**
     * @param handler will be notified about changes of the selected index
     * @return 
     */
    HandlerRegistration addSelectionChangedhandler(SelectionChangedHandler<K> handler);

    /**
     * @param handler will be notified if the content model list is changed.
     * @return 
     */
    HandlerRegistration addListChangedHandler(ListChangedHandler<K> handler);

}
