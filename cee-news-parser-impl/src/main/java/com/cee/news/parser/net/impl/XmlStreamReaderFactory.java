package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.cee.news.parser.net.ReaderFactory;
import com.sun.syndication.io.XmlReader;

public class XmlStreamReaderFactory implements ReaderFactory {

	@Override
	public Reader createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException {
		return new XmlReader(inputStream, contentTypeHint, true, characterEncodingHint);
	}

}
