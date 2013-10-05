package org.cee.parser.net.impl;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.protocol.HttpContext;

public class DefaultHttpClientFactory implements HttpClientFactory {

    @Override
    public HttpClient createHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        
        //Proxy settings
        HttpRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
            client.getConnectionManager().getSchemeRegistry(),
            ProxySelector.getDefault()
        );
        client.setRoutePlanner(routePlanner);
        
        //Keep location changes for redirects
        client.addResponseInterceptor(new HttpResponseInterceptor() {
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
        });
        
        return client;
    }

}
