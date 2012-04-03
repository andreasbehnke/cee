package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cee.news.model.EntityKey;

/**
 * 
 * @author andreasbehnke
 * @deprecated This class should be deleted after refactoring
 */
@Deprecated
public final class EntityKeyUtil {
	
	private EntityKeyUtil() {}

    public static List<String> extractKeys(Collection<EntityKey> keys) {
    	List<String> result = new ArrayList<String>();
    	for (EntityKey entityKey : keys) {
			result.add(entityKey.getKey());
		}
    	return result;
    }
    
    public static List<EntityKey> createKeys(Collection<String> names) {
        List<EntityKey> keys = new ArrayList<EntityKey>();
        for (String name : names) {
            keys.add(createEntityKey(name, name));
        }
        return keys;
    }
    
    public static EntityKey createEntityKey(String name, String key) {
        return new EntityKey(name, key);
    }
}
