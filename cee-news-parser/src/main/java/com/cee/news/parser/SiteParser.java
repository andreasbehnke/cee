package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;

import com.cee.news.SiteExtraction;

public interface SiteParser {
	
	SiteExtraction parse(URL siteLocation) throws ParserException, IOException;

}
