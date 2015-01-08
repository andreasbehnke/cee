/**
 * 
 */
package org.cee.webreader.client.ui;

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

import org.cee.store.EntityKey;
import org.cee.webreader.client.workingset.WorkingSetSelectionView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
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
			UiBinder<HTMLPanel, WorkingSetSelection> {
	}

	@UiField
	Button newButton;
	
	@UiField
	Button editButton;
	
	@UiField
	Button deleteButton;
	
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
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@Override
	public void setEditButtonEnabled(boolean enabled) {
		editButton.setEnabled(enabled);
	}
	
	@Override
	public void setDeleteButtonEnabled(boolean enabled) {
		deleteButton.setEnabled(enabled);
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
