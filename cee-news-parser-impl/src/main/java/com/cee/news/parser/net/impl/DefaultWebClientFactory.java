package com.cee.news.parser.net.impl;

import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebClientFactory;

public class DefaultWebClientFactory implements WebClientFactory {
	
	private HttpClientFactory httpClientFactory;
	
	private ReaderFactory readerFactory;
	
	public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	public void setReaderFactory(ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}

	@Override
    public WebClient createWebClient() {
	    return new DefaultWebClient(httpClientFactory, readerFactory);
    }

	
}
