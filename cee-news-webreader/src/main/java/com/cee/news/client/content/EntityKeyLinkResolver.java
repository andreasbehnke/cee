package com.cee.news.client.content;

import com.cee.news.client.list.KeyLink;
import com.cee.news.client.list.KeyLinkResolver;
import com.cee.news.model.EntityKey;

public class EntityKeyLinkResolver implements KeyLinkResolver<EntityKey> {

    @Override
    public KeyLink createLink(EntityKey key) {
        return new KeyLink(key.getName(), key.getKey());
    }

    @Override
    public EntityKey resolveKey(KeyLink link) {
        return new EntityKey(link.getText(), link.getKeyValue());
    }

}
