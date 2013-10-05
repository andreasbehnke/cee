package org.cee.parser;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.SiteExtraction;

public interface SiteParser {
	
	SiteExtraction parse(Reader input, URL siteLocation) throws ParserException, IOException;

}
