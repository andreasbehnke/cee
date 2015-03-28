package org.cee.parser.impl;

import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class TeeContentHandler implements ContentHandler {
    
    private List<ContentHandler> delegates;
    
    public TeeContentHandler(ContentHandler... delegates) {
        this.delegates = Arrays.asList(delegates);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        for (ContentHandler delegate : this.delegates) {
            delegate.setDocumentLocator(locator);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.startDocument();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.endDocument();
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.startPrefixMapping(prefix, uri);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.endPrefixMapping(prefix);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.endElement(uri, localName, qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.characters(ch, start, length);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.ignorableWhitespace(ch, start, length);
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.processingInstruction(target, data);
        }
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        for (ContentHandler delegate : this.delegates) {
            delegate.skippedEntity(name);
        }
    }

}
