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


import java.util.Collection;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Model of a list with selection support. Multiple elements of the content list can be selected.
 */
public interface MultiSelectListModel<K> extends ListModel<K> {
    
    /**
     * Adds the element with key to the selection list
     * @param key The key of the element to be added
     */
    void addSelection(K key);
    
    /**
     * Removes the element with key from the selection list
     * @param key The key of the element to be removed
     */
    void removeSelection(K key);
    
    Collection<K> getSelections();
    
    /**
     * Set the selected keys
     * @param selections List containing the selected keys
     */
    void setSelections(Collection<K> selections);
    
    /**
     * Clears the current selection
     */
    void clearSelection();
    
    /**
     * @param handler will be notified if the selection list is changed.
     */
    HandlerRegistration addSelectionListChangedHandler(SelectionListChangedHandler<K> handler);
}
