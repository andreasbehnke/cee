package com.cee.news.parser.net;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * Provides access to web resources. A web client manages connection state, this
 * includes authentication and cookies of HTTP connections.
 */
public interface WebClient {
    
    WebResponse openWebResponse(URL location);
    
    Reader openReader(URL location) throws IOException;
}
