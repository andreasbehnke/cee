package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Provides access to web resources. A web client manages connection state, this
 * includes authentication and cookies of HTTP connections.
 */
public interface WebClient {

    /**
     * Opens a stream for the given URL
     * @param location Location of the resource
     * @return {@link InputStream} of the web resource. Caller is responsible for closing the stream.
     * @throws IOException If the input stream could not be opened
     * 
     * @deprecated use openWebResponse.getStream instead
     */
	@Deprecated
    InputStream openStream(URL location) throws IOException;
    
    WebResponse openWebResponse(URL location) throws IOException;
    
}
