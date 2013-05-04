package com.cee.news.client.ui;

import com.cee.news.client.NotificationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class Notification extends PopupPanel implements NotificationView {
	
	public interface NotificationUiBinder extends UiBinder<Widget, Notification> {}
    
	private static NotificationUiBinder uiBinder = GWT.create(NotificationUiBinder.class);
	
	private static Resources resources = GWT.create(Resources.class);

	@UiField
	InlineLabel labelTitle;
	
	@UiField
	InlineLabel labelMessage;
	
	@UiField
	Button buttonOk;
	
	public Notification() {
		setWidget(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setStyleName(resources.styles().popupPanel());
	}

	@Override
	public HasClickHandlers getButtonOk() {
		return buttonOk;
	}

	@Override
	public HasText getLabelTitle() {
		return labelTitle;
	}
	
	@Override
	public HasText getLabelMessage() {
		return labelMessage;
	}
}
