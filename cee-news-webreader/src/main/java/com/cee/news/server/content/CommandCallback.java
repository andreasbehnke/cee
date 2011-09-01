package com.cee.news.server.content;

public interface CommandCallback {
	
	void notifyFinished();

	void notifyError(Exception ex);
}
