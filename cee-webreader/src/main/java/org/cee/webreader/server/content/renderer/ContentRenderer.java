package org.cee.webreader.server.content.renderer;

import java.util.List;

import org.cee.webreader.client.content.EntityContent;

public interface ContentRenderer<K,T> {
    
    EntityContent<K> render(K key, T object, String templateName);
    
    List<EntityContent<K>> render(List<K> keys, List<T> objects, String templateName);

}
