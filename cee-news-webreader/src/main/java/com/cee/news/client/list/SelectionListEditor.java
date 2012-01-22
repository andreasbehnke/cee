package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;

/**
 * Editor adapter for the {@link SelectionChangedHandler}
 */
public class SelectionListEditor<K> implements LeafValueEditor<List<K>>, SelectionListChangedHandler<K> {
	
	private MultiSelectListModel<K> model;
	
	private List<K> selections = new ArrayList<K>();
	
	public SelectionListEditor(MultiSelectListModel<K> model) {
		this.model = model;
		model.addSelectionListChangedHandler(this);
	}

	@Override
	public void setValue(List<K> value) {
		model.setSelections(value);
		selections = value;
	}

	@Override
	public List<K> getValue() {
		return selections;
	}

	@Override
	public void onSelectionListChanged(SelectionListChangedEvent<K> event) {
		selections = event.getKeys();
	}
}