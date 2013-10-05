package org.cee.parser.net.impl;

import java.net.URL;

import org.apache.http.client.HttpClient;
import org.cee.parser.net.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link WebClient} implementation uses the {@link HttpClient} for HTTP
 * connections and falls back to the java.net API for all other protocols.
 */
public class DefaultWebClient extends BaseWebClient {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultWebClient.class);
	
    private static final String HTTP_PROTOCOL = "http";

    private static final String HTTPS_PROTOCOL = "https";

    private HttpClient httpClient;
    
    private ReaderFactory readerFactory;

    public DefaultWebClient() {
    }

    public DefaultWebClient(HttpClientFactory httpClientFactory, ReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
        setHttpClientFactory(httpClientFactory);
    }
    
    public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClient = httpClientFactory.createHttpClient();
    }

    public ReaderFactory getReaderFactory() {
		return readerFactory;
	}

	public void setReaderFactory(ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}

	public WebResponse openWebResponse(final URL location) {
		if (readerFactory == null) {
			throw new IllegalArgumentException("The property readerFactory has not been set yet!");
		}
		String protocol = location.getProtocol();
        if (protocol.equalsIgnoreCase(HTTP_PROTOCOL) || protocol.equalsIgnoreCase(HTTPS_PROTOCOL)) {
        	LOG.debug("open http response for {}", location);
        	if (httpClient == null) {
        		throw new IllegalArgumentException("The property httpClient has not been set yet!");
        	}
        	return new HttpWebResponse(location, httpClient, readerFactory);
        } else {
        	LOG.debug("open standard response for {}", location);
            return new DefaultWebResponse(location, readerFactory);
        }
    }
}
