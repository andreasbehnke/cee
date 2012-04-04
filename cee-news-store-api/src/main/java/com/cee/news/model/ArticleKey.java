package com.cee.news.model;

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
        return "[key=" + getKey() + ";siteKey=" + siteKey + ";name=" + getName() + "]";
    }
}

