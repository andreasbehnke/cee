package org.cee.webreader.client.list;

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