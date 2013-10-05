package org.cee.parser.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.cee.parser.net.impl.ClassResourceWebClient;
import org.junit.Assert;
import org.junit.Test;

public class TestClassResourceWebClient {

	private static final String HELLO_WORLD = "Hello World!";
	private static final String TESTSITE_URL = "http://www.testsite.com/org/cee/parser/net/impl/testSite.html";

	@Test
	public void testOpenStream() throws MalformedURLException, IOException {
		ClassResourceWebClient webClient = new ClassResourceWebClient();
		InputStream is = webClient.openWebResponse(new URL(TESTSITE_URL)).openStream();
		
		Reader reader = new InputStreamReader(is);
		char[] buffer = new char[12];
		Assert.assertEquals(12, reader.read(buffer));
		Assert.assertEquals(HELLO_WORLD, new String(buffer));
	}
}
