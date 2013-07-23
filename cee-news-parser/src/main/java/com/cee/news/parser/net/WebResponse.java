package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public interface WebResponse {
    
	/**
	 * Opens a new {@link InputStream} to response data.
	 * The implementation is responsible for handling multiple reads
	 * of data stream.
	 */
	InputStream openStream() throws IOException;
    
	/**
	 * Opens a new {@link Reader} to response data.
	 * The implementation is responsible for handling multiple reads
	 * of data stream.
	 */
    Reader openReader() throws IOException;

    String getContentType() throws IOException;
    
    String getContentEncoding() throws IOException;
    
    long getContentLength() throws IOException;
    
    URL getLocation() throws IOException;
}
