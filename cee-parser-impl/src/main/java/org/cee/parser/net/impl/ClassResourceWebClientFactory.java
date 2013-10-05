package org.cee.parser.net.impl;

import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;

public class ClassResourceWebClientFactory implements WebClientFactory {

	@Override
    public WebClient createWebClient() {
	    return new ClassResourceWebClient();
    }

}
