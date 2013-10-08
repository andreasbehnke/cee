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


public class TextBlock {
	
	private ContentExtractionMetaData metaData;

    private String content;

    public TextBlock(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Transient property containing meta data from the extraction process.
     * This object is provided for debugging purposes and will not be made persistent.
     */
	public ContentExtractionMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(ContentExtractionMetaData metaData) {
		this.metaData = metaData;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("TextBlock [content=").append(content);
		if (metaData != null) {
			b.append(", metaData=").append(metaData.toString());
		}
		b.append("]");
		return b.toString();
	}
}