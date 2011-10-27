package com.cee.news.server.content;

import org.junit.Test;

import com.sourceallies.beanoh.BeanohTestCase;

public class TestSpringConfiguration extends BeanohTestCase {
	
	@Test
    public void testContext() {  
        assertContextLoading();
    }
}
