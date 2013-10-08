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

import org.apache.commons.io.input.XmlStreamReader;
import org.cee.parser.net.ReaderSource;

public class XmlStreamReaderFactory implements ReaderFactory {

	@Override
	public ReaderSource createReader(InputStream inputStream, String contentTypeHint, String characterEncodingHint) throws IOException {
		if (characterEncodingHint == null) {
			characterEncodingHint = "UTF-8";
		}
		XmlStreamReader reader = new XmlStreamReader(inputStream, contentTypeHint, true, characterEncodingHint);
		return new ReaderSource(reader, reader.getEncoding());
	}

}
