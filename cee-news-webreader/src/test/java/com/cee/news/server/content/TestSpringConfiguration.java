package com.cee.news.server.content;

import org.junit.Ignore;
import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

@Ignore
public class TestSpringConfiguration extends BeanohTestCase {
	
	@Test
    public void testContext() {  
        assertContextLoading();
    }
}
