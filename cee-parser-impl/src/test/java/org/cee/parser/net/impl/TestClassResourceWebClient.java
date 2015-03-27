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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.cee.net.impl.ClassResourceWebClient;
import org.junit.Assert;
import org.junit.Test;

public class TestClassResourceWebClient {

	private static final String HELLO_WORLD = "Hello World!";
	private static final String TESTSITE_URL = "http://www.testsite.com/org/cee/parser/net/impl/testSite.html";

	@Test
	public void testOpenStream() throws MalformedURLException, IOException {
		ClassResourceWebClient webClient = new ClassResourceWebClient();
		InputStream is = webClient.openWebResponse(new URL(TESTSITE_URL), false).openStream();
		
		Reader reader = new InputStreamReader(is);
		char[] buffer = new char[12];
		Assert.assertEquals(12, reader.read(buffer));
		Assert.assertEquals(HELLO_WORLD, new String(buffer));
	}
}
