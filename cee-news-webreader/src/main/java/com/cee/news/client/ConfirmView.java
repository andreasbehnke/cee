package com.cee.news.client;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface ConfirmView extends DialogView {
	
	HasClickHandlers getButtonYes();
	
	HasClickHandlers getButtonNo();
	
	HasText getLabelQuestion();
	
}