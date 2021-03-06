package org.cee.net.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser
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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public class ReaderSource implements Closeable {

	private final Reader reader;
	
	private final String contentEncoding;

	public ReaderSource(final Reader reader, final String contentEncoding) {
	    this.reader = reader;
	    this.contentEncoding = contentEncoding;
    }

	public Reader getReader() {
		return reader;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * Closes the underlying reader. This method must not be
	 * called if the reader is being closed directly.
	 */
    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
