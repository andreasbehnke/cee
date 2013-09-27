/**
 * 
 */
package com.cee.news.server.content;

import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory which provides a unique name for every 
 * created thread by appending a counter to the given name prefix
 */
public class PrefixCountThreadFactory implements ThreadFactory {
	
	private int count = 0;
	
	private final String prefix;
	
	private final boolean daemon;
	
	public PrefixCountThreadFactory(String prefix, boolean daemon) {
		this.prefix = prefix;
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, prefix + "#" + count++);
		t.setDaemon(daemon);
		return t;
	}

}
