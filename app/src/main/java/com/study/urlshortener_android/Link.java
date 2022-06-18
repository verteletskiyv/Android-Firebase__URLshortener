package com.study.urlshortener_android;

public class Link {
    private String urlFull, urlShort;

    public Link() {
    }

    public Link(String url_long, String url_short) {
        this.urlFull = url_long;
        this.urlShort = url_short;
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
}
