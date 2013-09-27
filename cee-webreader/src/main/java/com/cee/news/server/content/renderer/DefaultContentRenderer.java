package com.cee.news.server.content.renderer;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.content.EntityContent;

public abstract class DefaultContentRenderer<K, T> implements ContentRenderer<K, T> {

    @Override
    public List<EntityContent<K>> render(List<K> keys, List<T> objects, String templateName) {
        if (keys == null) {
            throw new IllegalArgumentException("Parameter keys must not be null");
        }
        if (objects == null) {
            throw new IllegalArgumentException("Parameter objects must not be null");
        }
        int size = keys.size();
        if (size != objects.size()) {
            throw new IllegalArgumentException("The parameters keys and objects must have same size");
        }
        List<EntityContent<K>> contents = new ArrayList<EntityContent<K>>(size);
        for (int i=0; i < size; i++) {
            contents.add(render(keys.get(i), objects.get(i), templateName));
        }
        return contents;
    }
}