package com.cee.news.parser.net.impl;

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
    
    public HttpWebResponse(URL location, HttpClient httpClient, ReaderFactory readerFactory) {
    	super(readerFactory);
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
    
    private void executeRequest() throws IOException {
        HttpGet httpGet = new HttpGet(originalLocation.toExternalForm());
        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(httpGet, context);
        entity = response.getEntity();
        if (entity == null) {
            throw new IOException("No entity received for " + originalLocation.toExternalForm());
        }
        URL redirectUrl = (URL) context.getAttribute(HttpClientFactory.LAST_REDIRECT_URL);
        if (redirectUrl != null) {
            location = redirectUrl;
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
	public String getContentType() throws IOException {
		Header contentTypeHeader = getEntity().getContentType();
        if (contentTypeHeader != null) {
            return contentTypeHeader.getValue();
        }
        return null;
	}

	@Override
	public String getContentEncoding() throws IOException {
	    Header contentEncodingHeader = getEntity().getContentEncoding();
		if (contentEncodingHeader != null) {
			return contentEncodingHeader.getValue();
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
}