package org.cee.webreader.client.workingset;

import java.util.List;

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