package com.cee.news.client.list;

import java.util.List;

public final class EntityKeyUtil {
	
	private EntityKeyUtil() {}
	
    public static EntityKey getEntityKey(List<EntityKey> keys, String key) {
    	for (EntityKey entityKey : keys) {
			if(entityKey.getKey().equals(key)) {
				return entityKey;
			}
		}
    	throw new IllegalArgumentException("Unknown key: " + key);
    }

    public static int getIndexOfEntityKey(List<EntityKey> keys, String key) {
    	for(int i = 0; i < keys.size(); i++) {
			if(keys.get(i).getKey().equals(key)) {
				return i;
			}
		}
    	throw new IllegalArgumentException("Unknown key: " + key);
    }

	
}
