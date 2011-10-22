package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import com.cee.news.parser.net.ReaderFactory;
import com.cee.news.parser.net.WebResponse;

final class DefaultWebResponse implements WebResponse {
	
	private final URL location;
	
	private final ReaderFactory readerFactory;

	DefaultWebResponse(URL location, ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
		this.location = location;
	}

	@Override
	public InputStream getStream() throws IOException {
	    return location.openStream();
	}

	@Override
	public Reader getReader() throws IOException {
		return readerFactory.createReader(getStream(), null, null);
	}

	@Override
	public String getContentType() {
	    return null;
	}

	@Override
	public String getContentEncoding() {
		return null;
	}

	@Override
	public long getContentLength() {
	    return -1;
	}
}