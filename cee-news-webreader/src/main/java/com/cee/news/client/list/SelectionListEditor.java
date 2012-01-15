package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.editor.client.LeafValueEditor;

/**
 * Editor adapter for the {@link SelectionChangedHandler}
 */
public class SelectionListEditor implements LeafValueEditor<List<EntityKey>>, SelectionListChangedHandler {
	
	private MultiSelectListModel model;
	
	private List<EntityKey> selections = new ArrayList<EntityKey>();
	
	public SelectionListEditor(MultiSelectListModel model) {
		this.model = model;
		model.addSelectionListChangedHandler(this);
	}

	@Override
	public void setValue(List<EntityKey> value) {
		model.setSelections(EntityKeyUtil.extractKeys(value));
	}

	@Override
	public List<EntityKey> getValue() {
		return selections;
	}

	@Override
	public void onSelectionListChanged(SelectionListChangedEvent event) {
		selections = event.getKeys();
	}
}