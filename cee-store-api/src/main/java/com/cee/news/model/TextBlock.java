package com.cee.news.model;

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