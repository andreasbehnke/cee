package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


final class DefaultWebResponse extends BaseWebResponse {
	
	private final URL originalLocation;
	
	DefaultWebResponse(URL location, ReaderFactory readerFactory) {
		super(readerFactory);
		this.originalLocation = location;
	}

	@Override
	protected InputStream openStreamInternal() throws IOException {
	    return originalLocation.openStream();
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
	
	@Override
	public URL getLocation() {
	    return originalLocation;
	}
}