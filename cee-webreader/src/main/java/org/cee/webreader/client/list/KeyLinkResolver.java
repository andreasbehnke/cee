package org.cee.webreader.client.list;

public interface KeyLinkResolver<K> {

    KeyLink createLink(K key);
    
    K resolveKey(KeyLink link);

}
