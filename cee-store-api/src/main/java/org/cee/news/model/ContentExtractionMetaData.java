package org.cee.news.model;

/*
 * #%L
 * Content Extraction Engine - News Store API
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
