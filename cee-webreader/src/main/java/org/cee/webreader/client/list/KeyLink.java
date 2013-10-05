package org.cee.webreader.client.list;

public class KeyLink {

    private String text;
    
    private String keyValue;

    public KeyLink() {}
    
    public KeyLink(String text, String keyValue) {
        super();
        this.text = text;
        this.keyValue = keyValue;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
