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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.cee.net.WebClient;
import org.cee.net.WebResponse;

/**
 * The class resource web client tries to map any request to a resource lookup to a local class path lookup.
 * Therefore only the PATH segment of the URL is used to find a resource. This implementation of the WebClient
 * is used for testing purposes.
 */
public class ClassResourceWebClient implements  WebClient {
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    @Override
    public boolean isSupported(URL location) {
        return true;
    }
    
	/**
	 * Only the path part of the URL is interpreted for retrieving resource from class path
	 * @see org.cee.net.WebClient#openWebResponse(java.net.URL)
	 */
	@Override
	public WebResponse openWebResponse(final URL location, boolean bufferStream) {
		return new WebResponse() {

			@Override
			public InputStream openStream() throws IOException {
				String path = location.getPath();
				InputStream is = getClass().getResourceAsStream(path);
				if (is == null) {
					throw new FileNotFoundException(path);
				}
				return is;
			}

			@Override
			public Reader openReader() throws IOException {
				return new InputStreamReader(openStream());
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

			@Override
			public URL getLocation() {
			    return location;
			}
			
			@Override
			public URL getOriginalLocation() {
				return location;
			}
		};
	}
	
	@Override
	public <T> Future<T> processWebResponse(final URL location, final boolean bufferStream, Function<WebResponse, T> responseProcessor) {
		return executorService.submit( () -> { return responseProcessor.apply(openWebResponse(location, bufferStream)); } );
	}
	
	@Override
	public void shutdown() {
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}
}
