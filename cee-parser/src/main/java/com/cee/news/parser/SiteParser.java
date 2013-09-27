package com.cee.news.parser;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import com.cee.news.SiteExtraction;

public interface SiteParser {
	
	SiteExtraction parse(Reader input, URL siteLocation) throws ParserException, IOException;

}
