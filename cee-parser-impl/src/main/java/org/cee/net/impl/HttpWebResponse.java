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
import java.io.InputStream;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


/**
 * WebResponse implementation for HTTP
 */
public final class HttpWebResponse extends BaseWebResponse {
	
	private final URL originalLocation;
	
	private final HttpClient httpClient;
	
	private HttpEntity entity;
	
	private URL location;
    
    public HttpWebResponse(URL location, HttpClient httpClient, ReaderFactory readerFactory, boolean bufferStream) {
    	super(readerFactory, bufferStream);
		if (location == null) {
			throw new IllegalArgumentException("Paramter location must not be null!");
		}
		if (httpClient == null) {
			throw new IllegalArgumentException("Paramter httpClient must not be null!");
		}
		if (readerFactory == null) {
			throw new IllegalArgumentException("Parameter readerFactory must not be null");
		}
		this.originalLocation = location;
		this.location = location;
	    this.httpClient = httpClient;
	}
    
    private String getUrlWithoutHash(URL location) {
        String result = location.toExternalForm();
        int hashIndex = result.indexOf('#');
        if (hashIndex > -1) {
            return result.substring(0, hashIndex);
        }
        return result;
    }
    
    private void executeRequest() throws IOException {
        HttpGet httpGet = new HttpGet(getUrlWithoutHash(originalLocation));
        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(httpGet, context);
        entity = response.getEntity();
        if (entity == null) {
            throw new IOException("No entity received for " + originalLocation.toExternalForm());
        }
        String redirectUrl = (String)context.getAttribute(HttpClientFactory.LAST_REDIRECT_URL);
        if (redirectUrl != null) {
            location = new URL(originalLocation, redirectUrl);
        }
    }
	
    private HttpEntity getEntity() throws IOException {
        if (entity == null) {
            executeRequest();
        }
        return entity;
    }
    
    @Override
    protected InputStream openStreamInternal() throws IOException {
    	return getEntity().getContent();
    }
    
    @Override
    protected String getContentEncodingHint() throws IOException {
        Header contentEncodingHeader = getEntity().getContentEncoding();
        if (contentEncodingHeader != null) {
            return contentEncodingHeader.getValue();
        }
        return null;
    }

	@Override
	public String getContentType() throws IOException {
		Header contentTypeHeader = getEntity().getContentType();
        if (contentTypeHeader != null) {
            return contentTypeHeader.getValue();
        }
        return null;
	}

	@Override
	public long getContentLength() throws IOException {
	    return getEntity().getContentLength();
	}
	
	@Override
	public URL getLocation() throws IOException {
	    if (entity == null) {
            executeRequest();
	    }
        return location;
	}
	
	@Override
	public URL getOriginalLocation() {
		return originalLocation;
	}
}