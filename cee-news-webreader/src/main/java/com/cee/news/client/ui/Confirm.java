package com.cee.news.client.ui;

import com.cee.news.client.ConfirmView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class Confirm extends PopupPanel implements ConfirmView {
	
	public interface ConfirmUiBinder extends UiBinder<Widget, Confirm> {}
    
	private static ConfirmUiBinder uiBinder = GWT.create(ConfirmUiBinder.class);
	
	private static Resources resources = GWT.create(Resources.class);
	
	@UiField
	InlineLabel labelTitle;
	
	@UiField
	InlineLabel labelQuestion;
	
	@UiField
	Button buttonYes;
	
	@UiField
	Button buttonNo;
	
	public Confirm() {
		setWidget(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setStyleName(resources.styles().popupPanel());
	}

	@Override
	public HasClickHandlers getButtonYes() {
		return buttonYes;
	}

	@Override
	public HasClickHandlers getButtonNo() {
		return buttonNo;
	}
	
	@Override
	public HasText getLabelTitle() {
		return labelTitle;
	}

	@Override
	public HasText getLabelQuestion() {
		return labelQuestion;
	}

}
