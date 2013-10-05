package org.cee.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface WebResponse {
    
	/**
	 * Opens a new {@link InputStream} to response data.
	 * The implementation is responsible for handling multiple reads
	 * of data stream.
	 */
	InputStream openStream() throws IOException;
    
	/**
	 * Opens a new {@link ReaderSource} to response data.
	 * The implementation is responsible for handling multiple reads
	 * of data stream.
	 */
    ReaderSource openReaderSource() throws IOException;

    String getContentType() throws IOException;
    
    String getContentEncoding() throws IOException;
    
    long getContentLength() throws IOException;
    
    URL getLocation() throws IOException;
}
