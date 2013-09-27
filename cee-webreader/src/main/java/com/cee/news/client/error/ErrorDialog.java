package com.cee.news.client.error;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class ErrorDialog extends DialogBox implements ErrorHandler {

	private final InlineLabel labelErrorMessage;
	
	public ErrorDialog() {
		setHTML("System Error");
		
		LayoutPanel layoutPanel = new LayoutPanel();
		setWidget(layoutPanel);
		layoutPanel.setSize("574px", "312px");
		
		InlineLabel nlnlblASystemError = new InlineLabel("A System Error occured:");
		layoutPanel.add(nlnlblASystemError);
		layoutPanel.setWidgetTopHeight(nlnlblASystemError, 0.0, Unit.PX, 21.0, Unit.PX);
		layoutPanel.setWidgetLeftRight(nlnlblASystemError, 0.0, Unit.PX, 0.0, Unit.PX);
		
		InlineLabel nlnlblErrorMessage = new InlineLabel("Error Message:");
		layoutPanel.add(nlnlblErrorMessage);
		layoutPanel.setWidgetLeftWidth(nlnlblErrorMessage, 0.0, Unit.PX, 90.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(nlnlblErrorMessage, 21.0, Unit.PX, 16.0, Unit.PX);
		
		labelErrorMessage = new InlineLabel("");
		layoutPanel.add(labelErrorMessage);
		layoutPanel.setWidgetLeftRight(labelErrorMessage, 0.0, Unit.PX, 0.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(labelErrorMessage, 43.0, Unit.PX, 37.0, Unit.PX);
		
		Button buttonOk = new Button("OK");
		layoutPanel.add(buttonOk);
		layoutPanel.setWidgetLeftWidth(buttonOk, 484.0, Unit.PX, 78.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(buttonOk, 281.0, Unit.PX, 24.0, Unit.PX);
		
		buttonOk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
	}

	@Override
	public void onError(ErrorEvent event) {
		labelErrorMessage.setText(event.getDescription() + "<br>" + event.getCause());
		show();
	}
}
