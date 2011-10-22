package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.parser.net.ReaderFactory;
import com.cee.news.parser.net.WebResponse;

/**
 * WebResponse implementation for HTTP
 */
public final class HttpWebResponse implements WebResponse {
	
	private final Logger LOG = LoggerFactory.getLogger(HttpWebResponse.class);
	
	private final HttpEntity entity;
	
	private final ReaderFactory readerFactory;

	public HttpWebResponse(URL location, HttpClient httpClient, ReaderFactory readerFactory) throws IOException {
		if (location == null) {
			throw new IllegalArgumentException("Paramter location must not be null!");
		}
		if (httpClient == null) {
			throw new IllegalArgumentException("Paramter httpClient must not be null!");
		}
		if (readerFactory == null) {
			throw new IllegalArgumentException("Parameter readerFactory must not be null");
		}
		this.entity = getHttpEntity(location, httpClient);
		this.readerFactory = readerFactory;
	}
	
    protected HttpEntity getHttpEntity(URL location, HttpClient httpClient) throws IOException {
        HttpGet httpGet = new HttpGet(location.toExternalForm());
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("No entity received for " + location.toExternalForm());
        }
        LOG.debug("retrieved http entity for {}", location);
        return entity;
    }

	@Override
	public InputStream getStream() throws IOException {
	    return entity.getContent();
	}

	@Override
	public Reader getReader() throws IOException {
		return readerFactory.createReader(getStream(), getContentType(), getContentEncoding());
	}

	@Override
	public String getContentType() {
		Header contentTypeHeader = entity.getContentType();
		if (contentTypeHeader != null) {
			return contentTypeHeader.getValue();
		}
	    return null;
	}

	@Override
	public String getContentEncoding() {
		Header contentEncodingHeader = entity.getContentEncoding();
		if (contentEncodingHeader != null) {
			return contentEncodingHeader.getValue();
		}
		return null;
	}

	@Override
	public long getContentLength() {
	    return entity.getContentLength();
	}
}