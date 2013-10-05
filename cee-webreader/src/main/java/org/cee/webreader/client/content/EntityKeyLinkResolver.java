package org.cee.webreader.client.content;

import org.cee.news.model.EntityKey;
import org.cee.webreader.client.list.KeyLink;
import org.cee.webreader.client.list.KeyLinkResolver;

public class EntityKeyLinkResolver implements KeyLinkResolver<EntityKey> {

    @Override
    public KeyLink createLink(EntityKey key) {
        return new KeyLink(key.getName(), key.getKey());
    }

    @Override
    public EntityKey resolveKey(KeyLink link) {
        return EntityKey.get(link.getText(), link.getKeyValue());
    }

}
