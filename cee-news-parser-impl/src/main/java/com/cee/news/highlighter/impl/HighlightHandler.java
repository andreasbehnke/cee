package com.cee.news.highlighter.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.model.TextBlock;
import com.cee.news.model.TextBlock.ContentExtractionMetaData;

public class HighlightHandler extends DefaultHandler {
	
	private final List<ContentExtractionMetaData> metaDataList;
	
	private final Writer output;
	
	private final Settings settings;
	
	private int characterElementIdx = 0;

	public HighlightHandler(final List<TextBlock> blocks, final Writer output, final Settings settings) {
		this.output = output;
		this.settings = settings;
		this.metaDataList = new ArrayList<ContentExtractionMetaData>();
		for (TextBlock textBlock : blocks) {
			metaDataList.add(textBlock.getMetaData());
        }
	}
	
	private ContentExtractionMetaData getMetaData(int characterElementIdx) {
		for (ContentExtractionMetaData md : metaDataList) {
	        if (md.getContainedTextBlocks().get(characterElementIdx)) {
	        	return md;
	        }
        }
		return null;
	}
	
	private String xmlEncode(final String in) {
		if (in == null) {
			return "";
		}
		char c;
		StringBuilder out = new StringBuilder(in.length());

		for (int i = 0; i < in.length(); i++) {
			c = in.charAt(i);
			switch (c) {
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '&':
				out.append("&amp;");
				break;
			case '"':
				out.append("&quot;");
				break;
			default:
				out.append(c);
			}
		}

		return out.toString();
	}
	
	private void write(String text) {
		try {
	        output.write(text);
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}

	private void write(char[] ch, int start, int length) {
		try {
	        output.write(ch, start, length);
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}
	
	private void write(String qname, Attributes attributes) {
		try {
	        output.append('<')
	        	.append(qname);
	        for (int i = 0; i < attributes.getLength(); i++) {
				final String attr = attributes.getQName(i);
				final String value = attributes.getValue(i);
				output.append(' ')
					.append(attr)
					.append("=\"")
					.append(xmlEncode(value))
					.append("\"");
			}
	        output.append('>');
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}
	
	private void writeEndTag(String qname) {
		try {
	        output.append("</")
	        	.append(qname)
	        	.append('>');
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characterElementIdx++;
		ContentExtractionMetaData metaData = getMetaData(characterElementIdx);
		boolean isTextBlock = metaData != null;
		boolean highlightContentBlock = isTextBlock && metaData.isContent() && settings.isHighlightContentBlock();
		if (highlightContentBlock) {
			write(settings.getContentBlockStart());
		}
		write(ch, start, length);
		if (highlightContentBlock) {
			write(settings.getContentBlockEnd());
		}
	}

	@Override
    public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
		write(qname, attributes);
    }

	@Override
    public void endElement(String uri, String localName, String qname) throws SAXException {
	    writeEndTag(qname);
    }
}