/**
 * 
 */
package com.cee.news.client.workingset.ui;

import java.util.List;

import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author andreasbehnke
 *
 */
public class WorkingSetSelection extends SimplePanel implements WorkingSetSelectionView {

	private static WorkingSetSelectionUiBinder uiBinder = GWT
			.create(WorkingSetSelectionUiBinder.class);

	interface WorkingSetSelectionUiBinder extends
			UiBinder<FlowPanel, WorkingSetSelection> {
	}

	@UiField
	Button newButton;
	
	@UiField
	Button editButton;
	
	@UiField
	ListBox listBox;
	
	public WorkingSetSelection() {
		setWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void addSelectionChangedHandler(ChangeHandler handler) {
		listBox.addChangeHandler(handler);
	}

	@Override
	public HasClickHandlers getNewButton() {
		return newButton;
	}

	@Override
	public HasClickHandlers getEditButton() {
		return editButton;
	}

	@Override
	public void setEditButtonEnabled(boolean enabled) {
		editButton.setEnabled(enabled);
	}

	@Override
	public int getSelectedWorkingSet() {
		return listBox.getSelectedIndex();
	}

	@Override
	public void setSelectedWorkingSet(int key) {
		listBox.setSelectedIndex(key);
	}

	@Override
	public void setWorkingSets(List<EntityKey> names) {
        listBox.clear();
        for (EntityKey name : names) {
            listBox.addItem(name.getName());
        }
	}

}
