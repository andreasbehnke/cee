package com.cee.news.highlighter.impl;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.model.TextBlock;
import com.cee.news.model.TextBlock.ContentExtractionMetaData;

public class HighlightHandler extends DefaultHandler {
	
	private static enum HtmlState {
		head,
		body
	}
	
	private static enum BlockState {
		start,
		firstTextOfBlock,
		inBlock
	}
	
	private final static String[] ATTRIBUTE_REWRITE = new String[] { "href", "src", "action", "archive", "classid", "codebase", "data", "formaction", 
		"background", "cite", "longdesc", "profile", "usemap", "icon", "manifest", "poster" };

	private final List<TextBlock> textBlocks;
	
	private final Stack<ContentExtractionMetaData> metaDataStack;
	
	private final BitSet containedTextBlocks;
	
	private final Writer output;
	
	private final Settings settings;
	
	private final TemplateCache templateCache;
	
	private final UUID namespace = UUID.randomUUID();
	
	private final Map<String, String> parameters;
	
	private HtmlState htmlState = HtmlState.head;
	
	private BlockState blockState = BlockState.start;
	
	private int characterElementIdx = 0;

	public HighlightHandler(final List<TextBlock> blocks, final Writer output, final Settings settings, final TemplateCache templateCache) {
		if (blocks == null) {
			throw new IllegalArgumentException("parameter blocks must not be null");
		}
		if (output == null) {
			throw new IllegalArgumentException("parameter output must not be null");
		}
		if (settings == null) {
			throw new IllegalArgumentException("parameter settings must not be null");
		}
		if (settings.getBaseUrl() == null && settings.isRewriteUrls()) {
			throw new IllegalArgumentException("Base URL setting required for rewriting URLs");
		}
		if ((settings.getMetadataContainerTemplate() != null && settings.getMetadataTemplate() == null) || (settings.getMetadataContainerTemplate() == null && settings.getMetadataTemplate() != null)) {
			throw new IllegalArgumentException("if setting metadataContainerTemplate is set, setting metadataTemplate must also be present");
		}
		if (templateCache == null) {
			throw new IllegalArgumentException("Parameter templateCache must not be null");
		}
		this.textBlocks = blocks;
		this.output = output;
		this.settings = settings;
		this.templateCache = templateCache;
		metaDataStack = new Stack<ContentExtractionMetaData>();
		containedTextBlocks = new BitSet();
		for (int i = blocks.size() -1; i > -1; i--) {
			ContentExtractionMetaData md = blocks.get(i).getMetaData();
			metaDataStack.add(md);
			containedTextBlocks.or(md.getContainedTextBlocks());
		}
		parameters = new HashMap<String, String>();
		parameters.put(Settings.PARAMETER_NAMESPACE, namespace.toString());
	}
	
	private boolean isContainedTextBlock() {
		return containedTextBlocks.get(characterElementIdx);
	}
	
	private ContentExtractionMetaData getMetaData() {
		ContentExtractionMetaData metaData = metaDataStack.peek();
		if (blockState == BlockState.start) {
			blockState = BlockState.firstTextOfBlock;
		} else {
			blockState = BlockState.inBlock;
		}
		while(!metaData.getContainedTextBlocks().get(characterElementIdx)) {
			blockState = BlockState.firstTextOfBlock;
			metaDataStack.pop();
			metaData = metaDataStack.peek();
		}
		return metaData;
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
	
	private void writeStartTag(String qname, Attributes attributes, String[] attributeOverrides) {
		try {
	        output.append('<')
	        	.append(qname);
	        for (int i = 0; i < attributes.getLength(); i++) {
				final String attr = attributes.getQName(i);
				String value = null;
				if (attributeOverrides != null) {
					value = attributeOverrides[i];
				}
				if (value == null) {
					value = attributes.getValue(i);
				}
				output.append(' ')
					.append(attr)
					.append("=\"")
					.append(value)
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
	
	private void writeTemplate(String template) {
		write(templateCache.fillTemplate(template, parameters));
	}
	
	private void writeHeaderTemplate() {
		writeTemplate(settings.getHeaderTemplate());
	}
	
	private void writeMetadataContainerTemplate() {
		StringBuilder content = new StringBuilder();
		for (TextBlock textBlock : textBlocks) {
			ContentExtractionMetaData metaData = textBlock.getMetaData();
	        parameters.put(Settings.PARAMETER_CONTENT, metaData.toString());
	        parameters.put(Settings.PARAMETER_ID, metaData.getId() + "");
	        content.append(templateCache.fillTemplate(settings.getMetadataTemplate(), parameters));
        }
		parameters.put(Settings.PARAMETER_CONTENT, content.toString());
		writeTemplate(settings.getMetadataContainerTemplate());
	}
	
	private void writeMetadataIconTemplate(ContentExtractionMetaData metaData) {
		parameters.put(Settings.PARAMETER_ID, metaData.getId() + "");
		writeTemplate(settings.getMetadataIconTemplate());
	}
	
	private void writeContentBlockTemplate(String content) {
		parameters.put(Settings.PARAMETER_CONTENT, content);
		writeTemplate(settings.getContentBlockTemplate());
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characterElementIdx++;
		boolean isTextBlock = (htmlState == HtmlState.body) && isContainedTextBlock();
		boolean highlightContentBlock = false;
		if (isTextBlock) {
			ContentExtractionMetaData metaData = getMetaData();
			highlightContentBlock = settings.isHighlightContentBlock() && metaData.isContent();
			boolean showMetadataIcon = settings.isShowBlockMetadata() && (blockState == BlockState.firstTextOfBlock);
			if (showMetadataIcon) {
				writeMetadataIconTemplate(metaData);
			}
			if (highlightContentBlock) {
				writeContentBlockTemplate(new String(ch, start, length));
			}
		} 
		if (!highlightContentBlock) {
			write(ch, start, length);
		}
	}
	
	private int getUrlAttribute(Attributes attributes) {
		for (String attribute : ATTRIBUTE_REWRITE) {
	        int index = attributes.getIndex(attribute);
	        if (index > -1) {
	        	return index;
	        }
        }
		return -1;
	}
	
	private String rewriteUrl(String spec) {
		try {
	        return new URL(settings.getBaseUrl(), spec).toExternalForm();
        } catch (MalformedURLException e) {
	        return spec;
        }
	}
	
	private String getBaseUrl() {
		try {
	        return new URL(settings.getBaseUrl(), "/").toExternalForm();
        } catch (MalformedURLException e) {
	        return "/";
        }
	}

	@Override
    public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
		String element = qname.toLowerCase();
		String[] attributeOverrides = new String[attributes.getLength()];
		
		if ("body".equals(element)) {
			htmlState = HtmlState.body;
		}
		
		if (settings.isRewriteUrls()) {
			// rewrite URL
			int indexUrlAttribute = 0;
			if ((indexUrlAttribute = getUrlAttribute(attributes)) > -1) {
				String originalUrl = attributes.getValue(indexUrlAttribute);
				String rewrittenUrl = rewriteUrl(originalUrl);
				attributeOverrides[indexUrlAttribute] = rewrittenUrl;
			}
			
			// remove base tag
			if ((htmlState != HtmlState.body) && "base".equals(element)) {
				return;
			}
		}
		
		writeStartTag(qname, attributes, attributeOverrides);
    }

	@Override
    public void endElement(String uri, String localName, String qname) throws SAXException {
		String element = qname.toLowerCase();
		boolean isHeader = "head".equals(element);
		boolean isBody = "body".equals(element);
		if (isHeader && settings.isRewriteUrls()) {
			// add base tag
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute("", "", "href", "", getBaseUrl());
			writeStartTag("base", attributesImpl, null);
			writeEndTag("base");
		}
		if (isHeader && settings.getHeaderTemplate() != null) {
			writeHeaderTemplate();
		}
		if (isBody && settings.isShowBlockMetadata()) {
			writeMetadataContainerTemplate();
		}
		writeEndTag(qname);
    }
}