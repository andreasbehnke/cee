package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link WebClient} implementation uses the {@link HttpClient} for HTTP
 * connections and falls back to the java.net API for all other protocols.
 */
public class DefaultWebClient implements WebClient {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultWebClient.class);
	
    private static final String HTTP_PROTOCOL = "http";

    private static final String HTTPS_PROTOCOL = "https";

    private HttpClient httpClient;

    public DefaultWebClient() {
    }

    public DefaultWebClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public InputStream openStream(URL location) throws IOException {
        String protocol = location.getProtocol();
        if (protocol.equalsIgnoreCase(HTTP_PROTOCOL) || protocol.equalsIgnoreCase(HTTPS_PROTOCOL)) {
            LOG.debug("open http stream for {}", location);
        	return openHttpStream(location);
        } else {
        	LOG.debug("open standard stream for {}", location);
            return location.openStream();
        }
    }

    protected InputStream openHttpStream(URL location) throws IOException {
        if (httpClient == null) {
            throw new IllegalStateException("The property httpClient has not been set");
        }
        return getHttpEntity(location).getContent();
    }

    protected HttpEntity getHttpEntity(URL location) throws IOException {
        HttpGet httpGet = new HttpGet(location.toExternalForm());
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("No entity received for " + location.toExternalForm());
        }
        LOG.debug("retrieved http entity for {}", location);
        return entity;
    }

    public WebResponse openWebResponse(final URL location) throws IOException {
        String protocol = location.getProtocol();
        if (protocol.equalsIgnoreCase(HTTP_PROTOCOL) || protocol.equalsIgnoreCase(HTTPS_PROTOCOL)) {
        	LOG.debug("open http response for {}", location);
        	return openHttpWebResponse(location);
        } else {
        	LOG.debug("open standard response for {}", location);
            return new WebResponse() {
            	@Override
            	public InputStream getStream() throws IOException {
                    return DefaultWebClient.this.openStream(location);
                }
                
                @Override
                public Reader getReader() throws IOException {
                	return new InputStreamReader(getStream());
                }

                @Override
                public String getContentType() {
                    return null;
                }
                
                @Override
                public String getContentEncoding() {
                	return null;
                }

                @Override
                public long getContentLength() {
                    return -1;
                }
            };
        }
    }

    protected WebResponse openHttpWebResponse(final URL location) throws IOException {
        final HttpEntity entity = getHttpEntity(location);
        return new WebResponse() {

        	@Override
            public InputStream getStream() throws IOException {
                return entity.getContent();
            }
            
            @Override
            public Reader getReader() throws IOException {
            	return new InputStreamReader(getStream(), getContentEncoding());
            }

            @Override
            public String getContentType() {
                return entity.getContentType().getValue();
            }
            
            @Override
            public String getContentEncoding() {
            	return entity.getContentEncoding().getValue();
            }

            @Override
            public long getContentLength() {
                return entity.getContentLength();
            }
        };
    }
}
