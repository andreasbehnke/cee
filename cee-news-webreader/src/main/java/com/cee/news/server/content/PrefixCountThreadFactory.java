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
	
	public PrefixCountThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, prefix + "#" + count++);
	}

}
