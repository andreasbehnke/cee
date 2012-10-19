package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filter;
import org.xml.sax.InputSource;

import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.sun.syndication.io.SAXBuilder;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.impl.XmlFixerReader;

public abstract class RomeFeedBase extends WireFeedInput {

	private class EmptyElementFilter implements Filter {

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof Element) {
				Element element = (Element)obj;
				int attributeCount = element.getAttributes().size();
				int contentCount = element.getContentSize();
				boolean isEmpty = element.getTextTrim() == null || element.getTextTrim().isEmpty();
				if (isEmpty && attributeCount == 0 && contentCount == 0) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	private WebClient webClient;
	
    /**
     * @param webClient Client used to execute web requests
     */
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
	private void removeEmptyElements(Element element) {
		element.removeContent(new EmptyElementFilter());
		for (Object	obj : element.getContent()) {
			if (obj instanceof Element) {
				removeEmptyElements((Element)obj);
			}
		}
	}
	
	protected Document openDocument(final URL feedLocation) throws JDOMException, IOException {
		SAXBuilder saxBuilder = createSAXBuilder();
		WebResponse response = webClient.openWebResponse(feedLocation);
	    Document document = null;
	    Reader reader = null;
	    try {
	    	reader = new XmlFixerReader(response.getReader());
	    	document = saxBuilder.build(new InputSource(reader));
	    } finally {
	    	if (reader != null)
	    		reader.close();
	    }
	    removeEmptyElements(document.getRootElement());
	    return document;
	}
}