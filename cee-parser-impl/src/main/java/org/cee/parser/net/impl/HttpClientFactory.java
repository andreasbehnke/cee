package org.cee.parser.net.impl;

import org.apache.http.client.HttpClient;

public interface HttpClientFactory {
    
    final static String LAST_REDIRECT_URL = "last_redirect_url";

    HttpClient createHttpClient();
    
}
