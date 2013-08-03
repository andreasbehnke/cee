package com.cee.news.model;

import java.util.BitSet;

public class TextBlock {
	
	public static class ContentExtractionMetaData {
		
		private final int id;
		
		private final boolean isContent;
		
		private final String extractionInformation;

	    private final BitSet containedTextBlocks;

		public ContentExtractionMetaData(int id, boolean isContent, String extractionInformation, BitSet containedTextBlocks) {
			this.id = id;
	        this.isContent = isContent;
			this.extractionInformation = extractionInformation;
	        this.containedTextBlocks = containedTextBlocks;
        }
		
		public boolean isContent() {
	        return isContent;
        }

		public String getExtractionInformation() {
			return extractionInformation;
		}

		public BitSet getContainedTextBlocks() {
			return containedTextBlocks;
		}
		
		public int getId() {
	        return id;
        }
		
		@Override
		public String toString() {
		    return "is content: " + isContent + ", " + extractionInformation + ", contained: " + containedTextBlocks.toString();
		}
	}
	
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