package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;

/**
 * Editor adapter for the {@link SelectionChangedHandler}
 */
public class SelectionListEditor implements LeafValueEditor<List<String>>, SelectionListChangedHandler {
	
	private MultiSelectListModel model;
	
	private List<String> selections = new ArrayList<String>();
	
	public SelectionListEditor(MultiSelectListModel model) {
		this.model = model;
		model.addSelectionListChangedHandler(this);
	}

	@Override
	public void setValue(List<String> value) {
		model.setSelections(value);
	}

	@Override
	public List<String> getValue() {
		return selections;
	}

	@Override
	public void onSelectionListChanged(SelectionListChangedEvent event) {
		selections.clear();
		for (EntityKey link : event.getLinks()) {
			selections.add(link.getKey());
		}
	}
}