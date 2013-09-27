package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.DialogView;
import com.cee.news.client.EditorView;
import com.cee.news.client.list.ListView;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.cee.news.model.EntityKey;
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