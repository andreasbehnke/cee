package org.cee.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.XmlStreamReader;
import org.cee.parser.net.ReaderSource;

public class XmlStreamReaderFactory implements ReaderFactory {

	@Override
	public ReaderSource createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException {
		if (characterEncodingHint == null) {
			characterEncodingHint = "UTF-8";
		}
		XmlStreamReader reader = new XmlStreamReader(inputStream, contentTypeHint, true, characterEncodingHint);
		return new ReaderSource(reader, reader.getEncoding());
	}

}
