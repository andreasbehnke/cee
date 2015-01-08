package org.cee.highlighter.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import org.cee.highlighter.ContentHighlighter.Settings;
import org.cee.store.article.ContentExtractionMetaData;
import org.cee.store.article.TextBlock;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

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
	
	private final List<String> issues;
	
	private final Stack<ContentExtractionMetaData> metaDataStack;
	
	private final BitSet containedTextBlocks;
	
	private final HighlightWriter writer;

	private final Settings settings;
	
	private HtmlState htmlState = HtmlState.head;
	
	private BlockState blockState = BlockState.start;
	
	private int characterElementIdx = 0;

	public HighlightHandler(final List<TextBlock> blocks, final List<String> issues, final HighlightWriter writer, final Settings settings) {
		if (blocks == null) {
			throw new IllegalArgumentException("parameter blocks must not be null");
		}
		if (issues == null) {
			throw new IllegalArgumentException("parameter issues must not be null");
		}
		if (writer == null) {
			throw new IllegalArgumentException("parameter writer must not be null");
		}
		if (settings == null) {
			throw new IllegalArgumentException("parameter settings must not be null");
		}
		if (settings.getBaseUrl() == null && settings.isRewriteUrls()) {
			throw new IllegalArgumentException("Base URL setting required for rewriting URLs");
		}
		if (settings.isShowBlockMetadata() && (settings.getMetadataContainerTemplate() == null || settings.getMetadataTemplate() == null)) {
			throw new IllegalArgumentException("if setting showBlockMetadata = true, metadataTemplate and metaDataContainerTemplate must be present");
		}
		this.textBlocks = blocks;
		this.issues = issues;
		this.writer = writer;
		this.settings = settings;
		metaDataStack = new Stack<ContentExtractionMetaData>();
		containedTextBlocks = new BitSet();
		for (int i = blocks.size() -1; i > -1; i--) {
			ContentExtractionMetaData md = blocks.get(i).getMetaData();
			metaDataStack.add(md);
			containedTextBlocks.or(md.getContainedTextBlocks());
		}
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
				writer.writeMetadataIconTemplate(metaData);
			}
			if (highlightContentBlock) {
				writer.writeContentBlockTemplate(new String(ch, start, length));
			}
		} 
		if (!highlightContentBlock) {
			writer.write(ch, start, length);
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
		
		writer.writeStartTag(qname, attributes, attributeOverrides);
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
			writer.writeStartTag("base", attributesImpl, null);
			writer.writeEndTag("base");
		}
		if (isHeader && settings.getHeaderTemplate() != null) {
			writer.writeHeaderTemplate();
		}
		if (isBody && settings.isShowBlockMetadata()) {
			writer.writeMetadataContainerTemplate(textBlocks);
		}
		writer.writeEndTag(qname);
    }
}