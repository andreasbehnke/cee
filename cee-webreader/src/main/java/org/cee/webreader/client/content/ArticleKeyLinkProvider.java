package org.cee.webreader.client.content;

import org.cee.news.model.ArticleKey;
import org.cee.webreader.client.list.KeyLink;
import org.cee.webreader.client.list.KeyLinkResolver;

public class ArticleKeyLinkProvider implements KeyLinkResolver<ArticleKey> {
    
    private String encodeKey(ArticleKey key) {
        return key.getSiteKey() + "/" + key.getKey(); 
    }
   
    private ArticleKey decodeLink(KeyLink link) {
        String combinedKey = link.getKeyValue();
        int index = combinedKey.indexOf('/');
        String siteKey = combinedKey.substring(0, index);
        String articleKey = combinedKey.substring(index);
        return ArticleKey.get(link.getText(), articleKey, siteKey);
    }
    
    @Override
    public KeyLink createLink(ArticleKey key) {
        return new KeyLink(key.getName(), encodeKey(key));
    }
    
    @Override
    public ArticleKey resolveKey(KeyLink link) {
        return decodeLink(link);
    }

}
