package org.cee.webreader.server.content;

public interface CommandCallback {
	
	void notifyFinished();

	void notifyError(Exception ex);
}
