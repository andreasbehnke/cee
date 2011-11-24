package com.cee.news.client.list;

import com.google.gwt.view.client.ProvidesKey;

public class EntityContentKeyProvider implements ProvidesKey<EntityContent> {

	@Override
	public Object getKey(EntityContent item) {
		return (item == null) ? null : item.getKey();
	}

}
