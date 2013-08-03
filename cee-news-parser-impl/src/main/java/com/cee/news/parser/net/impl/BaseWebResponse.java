package com.cee.news.parser.net.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

import com.cee.news.parser.net.ReaderSource;
import com.cee.news.parser.net.WebResponse;


public abstract class BaseWebResponse implements WebResponse {
	
	private byte[] buffer;
	
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

	@Override
    public final ReaderSource openReaderSource() throws IOException {
    	return readerFactory.createReader(openStream(), getContentType(), getContentEncoding());
    }
}