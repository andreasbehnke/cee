package com.cee.news.client.list;

/**
 * Represents the text and the value of a link in the pageable view.
 */
public class LinkValue {
    
    private final int value;
    
    private final String text;
    
    public LinkValue(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
