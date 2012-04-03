package com.cee.news.client.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contents rendered content of an entity
 *
 * @param <K> type of the key
 */
public class EntityContent<K> implements Serializable {
    
    private static final long serialVersionUID = 1590790581332061891L;

    private K key;

    private String content;
    
    public EntityContent() {}
    
    public EntityContent(K key, String content) {
        this.key = key;
        this.content = content;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public static <K> List<K> getKeys(Collection<EntityContent<K>> contents) {
        List<K> keys = new ArrayList<K>();
        for (EntityContent<K> entityContent : contents) {
            keys.add(entityContent.getKey());
        }
        return keys;
    }
}
