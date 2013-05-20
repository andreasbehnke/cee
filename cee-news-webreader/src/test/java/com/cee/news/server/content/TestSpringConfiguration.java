package com.cee.news.server.content;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

@Ignore
public class TestSpringConfiguration extends BeanohTestCase {
	
	private final static String TEST_REPOSITORY_DIR = "target/repository1";
	
	@Test
    public void testContext() throws IOException {
		try {
			assertContextLoading();
		} finally {
			File repdir = new File(TEST_REPOSITORY_DIR);
			System.out.println("deleting repository directory: " + repdir.getAbsolutePath());
		}
    }
}
