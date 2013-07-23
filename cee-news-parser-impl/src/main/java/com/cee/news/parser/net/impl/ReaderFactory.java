package com.cee.news.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

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
	Reader createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException;
	
}
