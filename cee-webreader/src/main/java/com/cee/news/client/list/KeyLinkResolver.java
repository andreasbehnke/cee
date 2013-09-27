package com.cee.news.client.list;

public interface KeyLinkResolver<K> {

    KeyLink createLink(K key);
    
    K resolveKey(KeyLink link);

}
