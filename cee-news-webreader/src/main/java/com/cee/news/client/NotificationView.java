package com.cee.news.client;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface NotificationView extends DialogView {

	HasClickHandlers getButtonOk();
	
	HasText getLabelTitle();
	
	HasText getLabelMessage();

}
