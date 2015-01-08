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

import org.cee.store.article.ContentExtractionMetaData;
import org.cee.store.article.ContentExtractionMetaData.Property;

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