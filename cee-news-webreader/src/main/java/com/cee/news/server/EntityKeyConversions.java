package com.cee.news.server;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.list.EntityKey;
import com.cee.news.model.NamedKey;

public final class EntityKeyConversions {
	
	private EntityKeyConversions() {}
	
	public static List<EntityKey> createEntityKeys(List<NamedKey> namedKeys) {
		List<EntityKey> keys = new ArrayList<EntityKey>();
		for (NamedKey namedKey : namedKeys) {
			keys.add(new EntityKey(namedKey.getKey(), namedKey.getName()));
		}
		return keys;
	}
	
	public static List<NamedKey> createNamedKeys(List<EntityKey> entityKeys) {
		List<NamedKey> keys = new ArrayList<NamedKey>();
		for (EntityKey entityKey : entityKeys) {
			keys.add(new NamedKey(entityKey.getName(), entityKey.getKey()));
		}
		return keys;
	}
}
