package com.cee.news.server.content;

public interface Command extends Runnable {

	void addCommandCallback(CommandCallback callback);

}
