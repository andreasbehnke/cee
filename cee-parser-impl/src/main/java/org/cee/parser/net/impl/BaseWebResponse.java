package org.cee.parser.net.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.cee.parser.net.WebResponse;


public abstract class BaseWebResponse implements WebResponse {
	
	private byte[] buffer;
	
	private ReaderSource readerSource;
	
	private final ReaderFactory readerFactory;
	
	protected BaseWebResponse(final ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}
	
	protected abstract InputStream openStreamInternal() throws IOException;

	@Override
	public final InputStream openStream() throws IOException {
	    if (buffer == null) {
	    	// first call, open input stream and cache data
	    	// for multiple reads
	    	InputStream input = openStreamInternal();
	    	try {
		    	buffer = IOUtils.toByteArray(input);		
	    	} finally {
	    		IOUtils.closeQuietly(input);
	    	}
	    }
	    return new ByteArrayInputStream(buffer);
	}
	
	protected abstract String getContentEncodingHint() throws IOException;
	
	private ReaderSource getReaderSource() throws IOException {
	    if (readerSource == null) {
	        this.readerSource = readerFactory.createReader(openStream(), getContentType(), getContentEncodingHint());
	    }
	    return readerSource; 
	}
	
	@Override
	public String getContentEncoding() throws IOException {
	    return getReaderSource().getContentEncoding();
	}

	@Override
    public final Reader openReader() throws IOException {
    	return getReaderSource().getReader();
    }
}
