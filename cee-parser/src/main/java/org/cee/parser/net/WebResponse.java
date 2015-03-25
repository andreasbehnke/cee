package org.cee.parser.net;

/*
 * #%L
 * Content Extraction Engine - News Parser
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
import java.io.Reader;
import java.net.URL;

public interface WebResponse {
    
	/**
	 * Opens a new {@link InputStream} to response data.
	 * An illegal state exception is thrown if this method is
	 * called multiple times and this WebResponse implementation does
	 * not support multiple reads.
	 */
	InputStream openStream() throws IOException;
    
	/**
	 * Opens a new {@link Reader} to response data.
     * An illegal state exception is thrown if this method is
     * called multiple times and this WebResponse implementation does
     * not support multiple reads.
     */
    Reader openReader() throws IOException;

    String getContentType() throws IOException;
    
    String getContentEncoding() throws IOException;
    
    long getContentLength() throws IOException;
    
    URL getLocation() throws IOException;
}
