package org.cee.news.model;

/*
 * #%L
 * Content Extraction Engine - News Store API
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


public class ArticleKey extends EntityKey {

    private static final long serialVersionUID = -8191528655609154866L;
    
    private String siteKey;
    
    private double score;
    
    public ArticleKey() {}
    
    protected ArticleKey(String name, String key, String siteKey) {
        this(name, key, siteKey, -1);
    }
    
    protected ArticleKey(String name, String key, String siteKey, double score) {
        super(name, key);
        this.siteKey = siteKey;
        this.score = score;
    }
    
    public static ArticleKey get(String name, String key, String siteKey, double score) {
        return new ArticleKey(name, key, siteKey, score);
    }
    
    public static ArticleKey get(String name, String key, String siteKey) {
        return new ArticleKey(name, key, siteKey);
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteName) {
        this.siteKey = siteName;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((siteKey == null) ? 0 : siteKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArticleKey other = (ArticleKey) obj;
        if (siteKey == null) {
            if (other.siteKey != null)
                return false;
        } else if (!siteKey.equals(other.siteKey))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[key=" + getKey() + ";siteKey=" + siteKey + ";name=" + getName() + ";score=" + score + "]";
    }
}

