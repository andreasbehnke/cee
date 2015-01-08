package org.cee.webreader.client.content;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.cee.store.article.ArticleKey;
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
