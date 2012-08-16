package com.cee.news.parser.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * The class resource web client tries to map any request to a resource lookup to a local class path lookup.
 * Therefore only the PATH segment of the URL is used to find a resource. This implementation of the WebClient
 * is used for testing purposes.
 */
public class ClassResourceWebClient implements WebClient {
	/**
	 * Only the path part of the URL is interpreted for retrieving resource from class path
	 * @see com.cee.news.parser.net.WebClient#openWebResponse(java.net.URL)
	 */
	@Override
	public WebResponse openWebResponse(final URL location) {
		return new WebResponse() {
			
			@Override
			public InputStream getStream() throws IOException {
				String path = location.getPath();
				InputStream is = getClass().getResourceAsStream(path);
				if (is == null) {
					throw new FileNotFoundException(path);
				}
				return is;
			}
			
			@Override
			public Reader getReader() throws IOException {
				return new InputStreamReader(getStream());
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
			    return location;
			}
		};
	}

}
