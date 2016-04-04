package com.huangsuqing.popularmovies;

/**
 * Created by Suqing on 4/2/16.
 */
public class Trailer {
    private String url;
    private String label;
    private String id;

    public Trailer(String url, String label, String id) {
        this.url = url;
        this.label = label;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "url='" + url + '\'' +
                ", label='" + label + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
