package com.baleksan.util.http;


/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public enum HttpContentType {
    PLAIN("text/plain"),
    JSON("application/json"),
    HTML("text/html");

    private String name;

    HttpContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
