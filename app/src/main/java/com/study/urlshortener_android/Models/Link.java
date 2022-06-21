package com.study.urlshortener_android.Models;

public class Link {
    private String urlFull, urlShort, uid;

    public Link() {
    }

    public Link(String urlFull, String urlShort, String uid) {
        this.urlFull = urlFull;
        this.urlShort = urlShort;
        this.uid = uid;
    }

    public String getUrlFull() {
        return urlFull;
    }

    public void setUrlFull(String urlFull) {
        this.urlFull = urlFull;
    }

    public String getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
