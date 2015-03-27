package org.cee.net.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.IOException;
import java.net.ProxySelector;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;

public class DefaultHttpClientFactory implements HttpClientFactory {
    
    private final HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    
    private final class RedirectHttpResponseInterceptor implements HttpResponseInterceptor {
        
        //Keep location changes for redirects in context
        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            if (response.containsHeader("Location")) {
                Header[] locations = response.getHeaders("Location");
                if (locations.length > 0) {
                    URL location = new URL(locations[0].getValue());
                    context.setAttribute(LAST_REDIRECT_URL, location);
                }
            }
        }
    }

    @Override
    public HttpClient createHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                .addInterceptorFirst(new RedirectHttpResponseInterceptor())
                .build();
    }
}
