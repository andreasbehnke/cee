package com.cee.news.parser.net;

import java.io.IOException;
import java.net.URL;

/**
 * Provides access to web resources. A web client manages connection state, this
 * includes authentication and cookies of HTTP connections.
 */
public interface WebClient {
    
    WebResponse openWebResponse(URL location) throws IOException;
    
}
