package com.cee.news.parser.net.impl;

import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebClientFactory;

public class ClassResourceWebClientFactory implements WebClientFactory {

	@Override
    public WebClient createWebClient() {
	    return new ClassResourceWebClient();
    }

}
