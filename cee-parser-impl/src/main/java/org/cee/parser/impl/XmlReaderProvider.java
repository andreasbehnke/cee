package org.cee.parser.impl;

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
	
	protected XMLReader createXmlReader() {
		return xmlReaderFactory.createXmlReader();
	}

}
