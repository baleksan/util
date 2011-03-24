package com.baleksan.util.http;

import org.apache.http.HttpStatus;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public enum PermittedHttpStatusCode {
    TIMEOUT(HttpStatus.SC_GATEWAY_TIMEOUT),
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(HttpStatus.SC_SERVICE_UNAVAILABLE),
    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST);

    private int httpErrorCode;

    PermittedHttpStatusCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public Integer getHttpStatusCode() {
        return httpErrorCode;
    }
}
