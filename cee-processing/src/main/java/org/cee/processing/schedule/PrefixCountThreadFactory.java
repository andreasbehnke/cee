/**
 * 
 */
package org.cee.processing.schedule;

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
