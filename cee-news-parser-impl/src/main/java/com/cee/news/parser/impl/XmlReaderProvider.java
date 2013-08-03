package com.cee.news.parser.impl;

import org.xml.sax.XMLReader;

public class XmlReaderProvider {
	
	private SaxXmlReaderFactory xmlReaderFactory;
	
	public XmlReaderProvider() {}
	
	public XmlReaderProvider(SaxXmlReaderFactory xmlReaderFactory) {
	    this.xmlReaderFactory = xmlReaderFactory;
    }

	public void setXmlReaderFactory(SaxXmlReaderFactory xmlReaderFactory) {
		this.xmlReaderFactory = xmlReaderFactory;
	}
	
	public XMLReader createXmlReader() {
		return xmlReaderFactory.createXmlReader();
	}

}