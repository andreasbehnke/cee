package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;

public final class EntityKeyUtil {
	
	private EntityKeyUtil() {}
	
	public static boolean containsEntityKey(List<EntityKey> keys, String key) {
    	for (EntityKey entityKey : keys) {
			if(entityKey.getKey().equals(key)) {
				return true;
			}
		}
    	return false;
    }
	
    public static EntityKey getEntityKey(List<EntityKey> keys, String key) {
    	for (EntityKey entityKey : keys) {
			if(entityKey.getKey().equals(key)) {
				return entityKey;
			}
		}
    	return null;
    }

    public static int getIndexOfEntityKey(List<EntityKey> keys, String key) {
    	for(int i = 0; i < keys.size(); i++) {
			if(keys.get(i).getKey().equals(key)) {
				return i;
			}
		}
    	return -1;
    }

    public static List<String> extractKeys(List<EntityKey> keys) {
    	List<String> result = new ArrayList<String>();
    	for (EntityKey entityKey : keys) {
			result.add(entityKey.getKey());
		}
    	return result;
    }
	
}
