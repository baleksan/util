package com.baleksan.util;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface Scorable<T> extends Comparable<T> {
    float getScore();

    void setBucket(int bucket);

    int getBucket();
}
