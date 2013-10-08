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


import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the selection of content changed
 */
public class SelectionChangedEvent<K> extends GwtEvent<SelectionChangedHandler<K>> {
    
    public final static GwtEvent.Type<SelectionChangedHandler<?>> TYPE = new Type<SelectionChangedHandler<?>>();
    
    private final K key;
    
    private final boolean userAction;
    
    public SelectionChangedEvent() {
        key = null;
        userAction = false;
    }
    
    public SelectionChangedEvent(K key, boolean userAction) {
        this.key = key;
        this.userAction = userAction;
    }
    
    /**
     * @return Primary key of the selected entity
     */
    public K getKey() {
        return key;
    }
    
    /**
     * @return true if this event was triggered by an user action
     */
    public boolean isUserAction() {
		return userAction;
	}

    @SuppressWarnings({"unchecked", "rawtypes"})
	@Override
    public GwtEvent.Type<SelectionChangedHandler<K>> getAssociatedType() {
        return (GwtEvent.Type)TYPE;
    }

    @Override
    protected void dispatch(SelectionChangedHandler<K> handler) {
        handler.onSelectionChange(this);
    }    
}
