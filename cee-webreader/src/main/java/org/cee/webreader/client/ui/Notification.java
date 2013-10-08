package org.cee.webreader.client.ui;

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

import org.cee.webreader.client.NotificationView;

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
