package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.input.XmlStreamReader;;

public class XmlStreamReaderFactory implements ReaderFactory {

	@Override
	public Reader createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException {
		if (characterEncodingHint == null) {
			characterEncodingHint = "UTF-8";
		}
		return new XmlStreamReader(inputStream, contentTypeHint, true, characterEncodingHint);
	}

}
