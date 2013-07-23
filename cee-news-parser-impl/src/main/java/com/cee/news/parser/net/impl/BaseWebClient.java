package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import com.cee.news.parser.net.WebClient;

public abstract class BaseWebClient implements WebClient {

	@Override
	public Reader openReader(URL location) throws IOException {
	    return openWebResponse(location).openReader();
	}
}
