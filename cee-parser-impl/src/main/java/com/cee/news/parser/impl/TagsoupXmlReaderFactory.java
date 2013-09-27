package com.cee.news.parser.impl;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.XMLReader;

public class TagsoupXmlReaderFactory implements SaxXmlReaderFactory {

	@Override
	public XMLReader createXmlReader() {
		return new Parser();
	}

}
