package org.cee.parser.net.impl;

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

import org.cee.parser.net.ReaderSource;

/**
 * Provides factory methods for creating character readers
 */
public interface ReaderFactory {

	/**
	 * Creates a new reader for the input stream
	 * @param inputStream The input stream to read bytes from
	 * @param contentTypeHint A hint for the content type or null if unknown. Implementations should try to evaluate the character encoding by reading information form the stream.
	 * @param characterEncodingHint A hint for the character encoding or null if unknown. Implementations should try to evaluate the character encoding by reading information form the stream.
	 * @return A new reader using the best matching character encoding
	 * @throws IOException if there are problems when reading from input stream. An IOException should never been thrown if 
	 */
	ReaderSource createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException;
	
}
