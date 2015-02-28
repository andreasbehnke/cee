package org.cee.store.jcr;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

@Ignore
public class TestSpringConfiguration extends BeanohTestCase {
		
	@Test
    public void testContext() throws IOException {
		assertContextLoading();
    }
}
