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


final class DefaultWebResponse extends BaseWebResponse {
	
	private final URL originalLocation;
	
	DefaultWebResponse(URL location, ReaderFactory readerFactory, boolean bufferStream) {
		super(readerFactory, bufferStream);
		this.originalLocation = location;
	}

	@Override
	protected InputStream openStreamInternal() throws IOException {
	    return originalLocation.openStream();
	}
	
	@Override
	protected String getContentEncodingHint() {
	    return null;
	}

	@Override
	public String getContentType() {
	    return null;
	}

	@Override
	public long getContentLength() {
	    return -1;
	}
	
	@Override
	public URL getLocation() {
	    return originalLocation;
	}
	
	@Override
	public URL getOriginalLocation() {
		return originalLocation;
	}
}