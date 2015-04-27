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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class DefaultHttpClientFactory implements HttpClientFactory {
    
    private final static String USER_AGENT = "User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36";
    
    private final Collection<Header> defaultHeaders;
    
    private final HttpClientConnectionManager connectionManager;
    
    private final class RedirectHttpResponseInterceptor implements HttpResponseInterceptor {
        
        //Keep location changes for redirects in context
        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            if (response.containsHeader("Location")) {
                Header[] locations = response.getHeaders("Location");
                if (locations.length > 0) {
                    context.setAttribute(LAST_REDIRECT_URL, locations[0].getValue());
                }
            }
        }
    }
    
    public DefaultHttpClientFactory() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setDefaultMaxPerRoute(4);
        poolingConnectionManager.setMaxTotal(20);
        poolingConnectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(3000).build());
        this.connectionManager = poolingConnectionManager;
        defaultHeaders = new ArrayList<Header>();
        defaultHeaders.add(new BasicHeader(HTTP.USER_AGENT, USER_AGENT));
    }

    @Override
    public HttpClient createHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                .addInterceptorFirst(new RedirectHttpResponseInterceptor())
                .setDefaultHeaders(defaultHeaders)
                .build();
    }
}
