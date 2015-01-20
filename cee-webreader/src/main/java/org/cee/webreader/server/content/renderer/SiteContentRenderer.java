package org.cee.webreader.server.content.renderer;

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


import org.cee.client.site.SiteData;
import org.cee.store.EntityKey;
import org.cee.webreader.client.content.EntityContent;

public class SiteContentRenderer extends DefaultContentRenderer<EntityKey, SiteData> {

    @Override
    public EntityContent<EntityKey> render(EntityKey key, SiteData site, String templateName) {
        if (key == null) {
            throw new IllegalArgumentException("Parameter key must not be null");
        }
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        StringBuilder builder = new StringBuilder();
//        String title = site.getTitle();
//        String description = site.getDescription();
//        builder.append(title == null ? site.getLocation() : title);
        builder.append(site.getName());
//        if (description != null) {
//            builder.append("<p>").append(site.getDescription()).append("</p>");
//        }
        return new EntityContent<EntityKey>(key, builder.toString());
    }
}
