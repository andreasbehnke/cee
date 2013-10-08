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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired if the content list of the {@link PagingContentModel} changed
 */
public class ListChangedEvent<K> extends GwtEvent<ListChangedHandler<K>> {

    public static final GwtEvent.Type<ListChangedHandler<?>> TYPE = new Type<ListChangedHandler<?>>();
    
    private final List<K> values;
    
    public ListChangedEvent() {
        this.values = null;
    }
    
    public ListChangedEvent(final List<K> values) {
        this.values = values;
    }

    public List<K> getValues() {
        return values;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GwtEvent.Type<ListChangedHandler<K>> getAssociatedType() {
        return (GwtEvent.Type)TYPE;
    }

    @Override
    protected void dispatch(ListChangedHandler<K> handler) {
        handler.onContentListChanged(this);
    }
}