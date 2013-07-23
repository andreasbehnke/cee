package com.cee.news.parser.net;

import java.io.Reader;

public class ReaderSource {

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
}
