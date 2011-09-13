package com.cee.news.client.progress;

import com.google.gwt.user.client.ui.Label;

public class TextProgressView extends Label implements ProgressView {
	
	private String messageFormat = "Progress: %s";
	
	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	@Override
	public void setPercentReady(int percentReady) {
		setText(messageFormat.replace("%s", percentReady + ""));
	}
}
