package org.cee.highlighter.impl;

import org.cee.news.model.ContentExtractionMetaData;
import org.cee.news.model.ContentExtractionMetaData.Property;

/**
 * Provides simple HTML formatted output for {@link ContentExtractionMetaData} as a simple HTML definition list.
 */
public class ContentExtractionMetaDataFormatter {
	
	private static StringBuilder appendDefinition(StringBuilder builder, String name, Object value) {
		builder.append("<dt>").append(name).append("</dt>").append("<dd>").append(value).append("</dd>");
		return builder;
	}
	
	private static StringBuilder appendDefinition(StringBuilder builder, Property property) {
		builder.append("<dt>").append(property.getName()).append("</dt>");
		for (String value : property.getValues()) {
	        builder.append("<dd>").append(value).append("</dd>");
        }
		return builder;
	}

	public String format(ContentExtractionMetaData metaData) {
		StringBuilder builder = new StringBuilder();
		builder.append("<dl>");
		appendDefinition(builder, "id", metaData.getId());
		appendDefinition(builder, "is content", metaData.isContent());
		for (Property property : metaData.getProperties()) {
	        appendDefinition(builder, property);
        }
		appendDefinition(builder, "contained text blocks", metaData.getContainedTextBlocks());
		builder.append("</dl>");
		return builder.toString();
	}
}