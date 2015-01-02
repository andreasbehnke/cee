package org.cee.webreader.client.workingset;

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

import org.cee.client.workingset.WorkingSetData;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.DialogView;
import org.cee.webreader.client.EditorView;
import org.cee.webreader.client.list.ListView;
import org.cee.webreader.client.list.SelectionListChangedHandler;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasText;

public interface WorkingSetView extends EditorView<WorkingSetData>, DialogView {
    
    HasText getErrorText();

    HasClickHandlers getButtonSave();

	HasClickHandlers getButtonCancel();
	
	HasClickHandlers getButtonAddNewSite();
	
	HasClickHandlers getButtonRemoveAllSites();
	
	ListView getAvailableSitesList();
	
	ListView getSelectedSitesList();
	
	void setSelectedSites(List<EntityKey> selectedSites);

    void addSiteSelectionListChangedHandler(SelectionListChangedHandler<EntityKey> selectionListChangedHandler);
    
    void setAvailableLanguages(List<EntityKey> languages, EntityKey defaultLanguage);
    
    void addLanguageChangedHandler(ValueChangeHandler<EntityKey> handler);
}