package org.cee.crawler;

import java.net.URL;

import org.xml.sax.ContentHandler;

public interface PageHandler {

	ContentHandler[] onPageStart(URL location);
	
	void onPageFinished(URL location, ContentHandler[] contentHandlers);
}
