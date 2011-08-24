package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;

public final class EntityKeyUtil {
	
	private EntityKeyUtil() {}
	
    public static List<String> extractKeys(List<EntityKey> keys) {
    	List<String> result = new ArrayList<String>();
    	for (EntityKey entityKey : keys) {
			result.add(entityKey.getKey());
		}
    	return result;
    }
	
}
