package com.cee.news.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ContentExtractionMetaData {
	
	public static class Property {
		
		public final String name;
		
		public final List<String> values;
		
		public Property(String name, List<String> values) {
			if (name == null) {
				throw new IllegalArgumentException("Parameter name must not be null");
			}
			if (values == null) {
				throw new IllegalArgumentException("Parameter values must not be null");
			}
			this.name = name;
			this.values = values;
		}
		
		public Property(String name, Object value) {
			if (name == null) {
				throw new IllegalArgumentException("Parameter name must not be null");
			}
			this.name = name;
			this.values = new ArrayList<String>();
			if(value != null) {
				values.add(value.toString());
			} else {
				values.add(null);
			}
		}
		
		public String getName() {
			return name;
		}
		
		public List<String> getValues() {
			return values;
		}
	}

	private final int id;
	
	private final boolean isContent;
	
	private final List<Property> properties;

	private final BitSet containedTextBlocks;

	public ContentExtractionMetaData(int id, boolean isContent, List<Property> properties, BitSet containedTextBlocks) {
	    this.id = id;
	    this.isContent = isContent;
	    this.properties = properties;
	    this.containedTextBlocks = containedTextBlocks;
    }

	public int getId() {
		return id;
	}

	public boolean isContent() {
		return isContent;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public BitSet getContainedTextBlocks() {
		return containedTextBlocks;
	}
}
