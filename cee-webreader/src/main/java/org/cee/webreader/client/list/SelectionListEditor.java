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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;

/**
 * Editor adapter for the {@link SelectionChangedHandler}
 */
public class SelectionListEditor<K> implements LeafValueEditor<List<K>> {
	
	private List<K> selections = new ArrayList<K>();
	
	private List<SelectionListChangedHandler<K>> selectionListChangedHandlers = new ArrayList<SelectionListChangedHandler<K>>();
	
	@Override
	public void setValue(List<K> value) {
		selections = value;
		fireSelectionListChanged();
	}

	@Override
	public List<K> getValue() {
		return selections;
	}
	
	private void fireSelectionListChanged() {
	    SelectionListChangedEvent<K> event = new SelectionListChangedEvent<K>(selections);
	    for (SelectionListChangedHandler<K> handler : selectionListChangedHandlers) {
            handler.onSelectionListChanged(event);
        }
	}
	
	public void addSelectionListChangedHandler(SelectionListChangedHandler<K> selectionListChangedHandler) {
	    selectionListChangedHandlers.add(selectionListChangedHandler);
	}
	
	public void setSelections(List<K> selections) {
        this.selections = selections;
    }
}