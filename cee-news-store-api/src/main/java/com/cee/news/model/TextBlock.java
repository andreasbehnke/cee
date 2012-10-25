package com.cee.news.model;

public class TextBlock {

    private String content;

    private int numWords;

    public TextBlock(String content, int numWords) {
        this.content = content;
        this.numWords = numWords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

	@Override
	public String toString() {
		return "TextBlock [content=" + content + ", numWords=" + numWords + "]";
	}
}