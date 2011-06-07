package com.cee.news.client.workingset;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface WorkingSetView {

	HasClickHandlers getButtonSave();

	HasClickHandlers getButtonCancel();
	
	HasText getErrorText();
}