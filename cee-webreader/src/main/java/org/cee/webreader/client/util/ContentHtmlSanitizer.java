package org.cee.webreader.client.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * HTML Sanitizer supporting the following tags and attributes:
 * 
 * Supported Tags - Supported Attributes:
 * 
 * b - class 
 * em - class 
 * i - class 
 * hr - class 
 * ul - class 
 * ol - class 
 * li - class 
 * p - class 
 * h[1-6] - class
 * a - href, target, class 
 * img - src, class
 * 
 * @author andreasbehnke
 */
public final class ContentHtmlSanitizer implements HtmlSanitizer {
	
	private static final Set<String> TAG_WHITELIST = 
		new HashSet<String>(Arrays.asList("b", "em", "i", "h1", "h2", "h3", "h4", "h5", "h6", "hr","ul", "ol", "li", "p", "a", "img", "br"));

	private static final String[] DEFAULT_ATTRIBUTES = new String[]{"class"};

	private static final String[] A_ATTRIBUTES = new String[]{"class", "href", "target"};

	private static final String[] IMG_ATTRIBUTES = new String[]{"class", "src"};

	private final boolean escapeIllegalTags;
	
	public ContentHtmlSanitizer(boolean escapeIllegalTags) {
		this.escapeIllegalTags = escapeIllegalTags;
	}

	@Override
	public SafeHtml sanitize(String html) {
		StringBuilder sanitized = new StringBuilder();

		boolean firstSegment = true;
		for (String segment : html.split("<", -1)) {
			if (firstSegment) {
				firstSegment = false;
				sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
				continue;
			}

			int tagStart = 0; // will be 1 if this turns out to be an end tag.
			int tagEnd = segment.indexOf('>');
			String tag = null;
			String tagName = null;
			String tagAttributes = null;
			boolean isValidTag = false;
			if (tagEnd > 0) {
				if (segment.charAt(0) == '/') {
					tagStart = 1;
				}
				tag = segment.substring(tagStart, tagEnd);
				int index = 0;
				int tagLength = tag.length();
				//find tag name
				while(index <= tagLength) {
					if (index == tagLength || tag.charAt(index) == ' ' || tag.charAt(index) == '/') {
						tagName = tag.substring(0, index).toLowerCase();
						break;
					}
					index++;
				}
				if (tagName != null && TAG_WHITELIST.contains(tagName)) {
					isValidTag = true;
					tagAttributes = tag.substring(index, tag.length());
				}
			}

			if (isValidTag) {
				// append the tag, not escaping it
				if (tagStart == 0) {
					sanitized.append('<').append(tagName);
					String[] validAttributes = null;
					if (tagName.equalsIgnoreCase("A")) {
						validAttributes = A_ATTRIBUTES;
					} else if (tagName.equalsIgnoreCase("IMG")) {
						validAttributes = IMG_ATTRIBUTES;
					} else {
						validAttributes = DEFAULT_ATTRIBUTES;
					}
					appendAttributes(sanitized, tagAttributes, Arrays.asList(validAttributes));
					
				} else {
					// we had seen an end-tag
					sanitized.append("</").append(tagName);
				}
				sanitized.append('>').append(SafeHtmlUtils.htmlEscapeAllowEntities(segment.substring(tagEnd + 1)));
			} else {
				if (escapeIllegalTags) {
					// just escape the whole segment
					sanitized.append("&lt;").append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
				} else {
					sanitized.append(segment.substring(tagEnd + 1));
				}
			}
		}

		return new SafeContentString(sanitized.toString());
	}
	
	protected void appendAttributes(StringBuilder sanitized, String segment, List<String> validAttributes) {
		int lastSpace = -1;
		int nameStart = 0;
		int valueStart = -1;
		int valueEnd = -1;
		String attributeName = null;
		String attributeValue = null;
		for(int index=0; index<segment.length(); index++) {
			if (segment.charAt(index) == ' ') {
				lastSpace = index;
			} else if (segment.charAt(index) == '=') {
				//found attribute
				attributeName = segment.substring(nameStart, index).trim().toLowerCase();
				if (validAttributes.contains(attributeName)) {
					//found valid attribute, extract attribute value
					valueStart = -1;
					valueEnd = -1;
					while(index<segment.length()) {
						index++;
						if (segment.charAt(index) == '\'' || segment.charAt(index) == '"') {
							valueStart = index + 1;
							break;
						}
					}
					while(index<segment.length()) {
						index++;
						if (segment.charAt(index) == '\'' || segment.charAt(index) == '"') {
							valueEnd = index;
							break;
						}
					}
					if (valueStart > -1 && valueEnd > -1) {
						attributeValue = segment.substring(valueStart, valueEnd);
						sanitized.append(' ').append(attributeName).append("=\"").append(attributeValue).append("\"");
					}
				}
			} else if(lastSpace == index-1) {
				nameStart = index;
			}
		}
	}
}
