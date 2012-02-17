package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cee.news.model.EntityKey;

public final class EntityKeyUtil {
	
	private EntityKeyUtil() {}

    public static List<String> extractKeys(Collection<EntityKey> keys) {
    	List<String> result = new ArrayList<String>();
    	for (EntityKey entityKey : keys) {
			result.add(entityKey.getKey());
		}
    	return result;
    }
	
}
