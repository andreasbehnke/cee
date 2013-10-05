package org.cee.parser.net.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.parser.net.WebClient;

public abstract class BaseWebClient implements WebClient {

	@Override
	public Reader openReader(URL location) throws IOException {
	    return openWebResponse(location).openReaderSource().getReader();
	}
}
