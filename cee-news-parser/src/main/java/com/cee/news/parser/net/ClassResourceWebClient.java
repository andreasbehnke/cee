package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The class resource web client tries to map any request to a resource lookup to a lokal class path lookup.
 * Therefore only the PATH segment of the URL is used to find a resource. This implementation of the WebClient
 * is used for testing purposes.
 */
public class ClassResourceWebClient implements WebClient {

	/**
	 * Only the path part of the URL is interpreted for retrieving resource from class path
	 * @see com.cee.news.parser.net.WebClient#openStream(java.net.URL)
	 */
	@Override
	public InputStream openStream(final URL location) throws IOException {
		String path = location.getPath();
		return getClass().getResourceAsStream(path);
	}

	/**
	 * Only the path part of the URL is interpreted for retrieving resource from class path
	 * @see com.cee.news.parser.net.WebClient#openWebResponse(java.net.URL)
	 */
	@Override
	public WebResponse openWebResponse(final URL location) throws IOException {
		return new WebResponse() {
			
			@Override
			public InputStream openStream() throws IOException {
				return ClassResourceWebClient.this.openStream(location);
			}
			
			@Override
			public String getContentType() {
				return null;
			}
			
			@Override
			public long getContentLength() {
				return -1;
			}
		};
	}

}
