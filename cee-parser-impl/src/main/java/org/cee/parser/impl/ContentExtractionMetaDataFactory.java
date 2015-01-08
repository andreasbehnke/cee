package org.cee.parser.impl;

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

import java.util.ArrayList;
import java.util.List;

import org.cee.store.article.ContentExtractionMetaData;
import org.cee.store.article.ContentExtractionMetaData.Property;

import de.l3s.boilerpipe.document.TextBlock;

public class ContentExtractionMetaDataFactory {
    
    private List<Property> buildExtractionInformationMap(TextBlock textBlock) {
    	List<Property> properties = new ArrayList<ContentExtractionMetaData.Property>();
    	properties.add(new Property("tag level", textBlock.getTagLevel()));
    	properties.add(new Property("labels", textBlock.getLabels()));
    	properties.add(new Property("number of words", textBlock.getNumWords()));
    	properties.add(new Property("number of words in anchor", textBlock.getNumWordsInAnchorText()));
    	properties.add(new Property("text density", textBlock.getTextDensity()));
    	properties.add(new Property("link density", textBlock.getLinkDensity()));
    	return properties;
    }

	public ContentExtractionMetaData create(int id, TextBlock textBlock) {
		return new ContentExtractionMetaData(
				id, 
				textBlock.isContent(), 
				buildExtractionInformationMap(textBlock), 
				textBlock.getContainedTextElements());
	}

}
