package com.cee.news.server.content;

/**
 * Command which can be executed by the crawler
 */
public interface Command extends Runnable {
	
	/**
	 * @return The number of remaining tasks.
	 */
	int getRemainingTasks();

	/**
	 * Register a callback which will be called when this command finishes execution.
	 * @param callback The callback being notified about command finish and command errors
	 */
	void addCommandCallback(CommandCallback callback);

}
