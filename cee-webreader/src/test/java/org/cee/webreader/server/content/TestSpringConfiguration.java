package org.cee.webreader.server.content;

import java.io.IOException;

import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

public class TestSpringConfiguration extends BeanohTestCase {
		
	@Test
    public void testContext() throws IOException {
		assertContextLoading();
    }
}
