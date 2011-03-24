package com.baleksan.util.http;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class HttpError {
    private static final String prefix = "http-error-";
    private static final int offset = prefix.length();

    private int httpStatus;

    public HttpError(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpError(String code) {
        this.httpStatus = decodeStatus(code);
    }

    public static int decodeStatus(String code) {
        return Integer.parseInt(code.substring(offset));
    }

    public static String encodeStatus(int status) {
        return prefix + status;
    }

    public static boolean isHttpError(String response) {
        return response != null && response.startsWith(prefix);
    }
}
