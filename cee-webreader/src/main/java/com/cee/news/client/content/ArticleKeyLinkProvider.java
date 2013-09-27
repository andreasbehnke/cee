package com.cee.news.client.content;

import com.cee.news.client.list.KeyLink;
import com.cee.news.client.list.KeyLinkResolver;
import com.cee.news.model.ArticleKey;

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
