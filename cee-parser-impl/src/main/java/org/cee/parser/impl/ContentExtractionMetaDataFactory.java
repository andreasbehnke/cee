package org.cee.parser.impl;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.ContentExtractionMetaData;
import org.cee.news.model.ContentExtractionMetaData.Property;

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
