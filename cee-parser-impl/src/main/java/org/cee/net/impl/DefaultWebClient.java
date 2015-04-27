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


import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.apache.http.client.HttpClient;
import org.cee.net.WebClient;
import org.cee.net.WebResponse;
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
    
    private static final String FTP_PROTOCOL = "ftp";
    
    private final ConcurrentMap<InetAddress, ExecutorService> executorServiceByHost = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<String, AtomicInteger> threadCountOfDomain = new ConcurrentHashMap<>();

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
	
	private Thread createNewThread(String domain, Runnable runnable) {
		Thread t = new Thread(runnable);
		int count = threadCountOfDomain.computeIfAbsent(domain, (s) -> { return new AtomicInteger(0);}).incrementAndGet();
		t.setName(domain + "#" + count);
		return t;
	}
	
	private ExecutorService getExecutorServiceForUrl(URL location) throws UnknownHostException {
		String host = location.getHost();
		final InetAddress inetAddr = InetAddress.getByName(host);
		return executorServiceByHost.computeIfAbsent(inetAddr, (url) -> {
			return Executors.newFixedThreadPool(5, (r) -> { return this.createNewThread(inetAddr.toString(), r); } ); 
		});
	}
	
	@Override
	public boolean isSupported(URL location) {
	    String protocol = location.getProtocol();
        return protocol.equalsIgnoreCase(HTTP_PROTOCOL) 
                || protocol.equalsIgnoreCase(HTTPS_PROTOCOL) 
                || protocol.equalsIgnoreCase(FTP_PROTOCOL);
	}

	@Override
	public WebResponse openWebResponse(final URL location, boolean bufferStream) {
		if (readerFactory == null) {
			throw new IllegalArgumentException("The property readerFactory has not been set yet!");
		}
		String protocol = location.getProtocol();
        if (protocol.equalsIgnoreCase(HTTP_PROTOCOL) || protocol.equalsIgnoreCase(HTTPS_PROTOCOL)) {
        	LOG.debug("open http response for {}", location);
        	if (httpClient == null) {
        		throw new IllegalArgumentException("The property httpClient has not been set yet!");
        	}
        	return new HttpWebResponse(location, httpClient, readerFactory, bufferStream);
        } else if (protocol.equalsIgnoreCase(FTP_PROTOCOL)) {
        	LOG.debug("open ftp response for {}", location);
            return new DefaultWebResponse(location, readerFactory, bufferStream);
        }
        throw new IllegalArgumentException("Unsupported protocol: " + location);
    }
	
	@Override
	public <T> Future<T> processWebResponse(URL location, boolean bufferStream, Function<WebResponse, T> responseProcessor) {
		try {
			return getExecutorServiceForUrl(location).submit( () -> { return responseProcessor.apply(openWebResponse(location, bufferStream)); });
		} catch (UnknownHostException e) {
			throw new RuntimeException("Could no add processor to processing queue", e);
		}
	}
}
