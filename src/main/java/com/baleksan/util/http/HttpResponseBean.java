package com.baleksan.util.http;

import org.apache.http.HttpStatus;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class HttpResponseBean {
    public int statusCode;
    public String response;
    public HttpMethod method;

    public boolean isSuccess() {
        if (method.equals(HttpMethod.GET)) {
            return statusCode == HttpStatus.SC_OK;
        } else if (method.equals(HttpMethod.POST)) {
            return statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_NO_CONTENT;
        }

        return false;
    }
}
