package com.cee.news.client.workingset;

import com.cee.news.client.DialogView;
import com.cee.news.client.EditorView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface WorkingSetView extends EditorView<WorkingSetData>, DialogView {
    
    HasText getErrorText();

    HasClickHandlers getButtonSave();

	HasClickHandlers getButtonCancel();
	
	HasClickHandlers getButtonAddNewSite();
}