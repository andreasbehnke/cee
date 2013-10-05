package org.cee.webreader.client.content;

import com.google.gwt.view.client.ProvidesKey;

public class EntityContentKeyProvider<K> implements ProvidesKey<EntityContent<K>> {

	@Override
	public K getKey(EntityContent<K> item) {
		return item.getKey();
	}

}
